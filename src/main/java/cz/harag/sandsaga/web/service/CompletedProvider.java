package cz.harag.sandsaga.web.service;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import cz.harag.sandsaga.web.dto.InClaimDto;
import cz.harag.sandsaga.web.dto.InClaimEntryDto;
import cz.harag.sandsaga.web.dto.OutClaimDto;
import cz.harag.sandsaga.web.dto.OutClaimEntryDto;
import cz.harag.sandsaga.web.dto.OutCompletedDto;
import cz.harag.sandsaga.web.dto.InCompletedMultipart;
import cz.harag.sandsaga.web.dto.SandSagaScenario;
import cz.harag.sandsaga.web.model.CompletedEntity;
import cz.harag.sandsaga.web.model.UserEntity;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import org.jboss.logging.Logger;

/**
 * @author Patrik Harag
 * @version 2024-03-08
 */
@ApplicationScoped
public class CompletedProvider {

    private static final Logger LOGGER = Logger.getLogger(CompletedProvider.class);

    private final ApiLimit limitEntity = new ApiLimit(ApiLimit.EPOCH_DURATION_DAY, 10_000);
    private final ApiLimit limitAdditionalData = new ApiLimit(ApiLimit.EPOCH_DURATION_DAY, 100);

    @Inject
    SandSagaConfigProvider configProvider;

    @Inject
    LiveStatsProvider liveStatsProvider;

    @Transactional
    public Long store(InCompletedMultipart input, String ip, Principal userPrincipal) {
        LOGGER.info("Storing completed from IP: " + ip);

        // basic validation
        if (input.scenario == null) {
            throw new BadRequestException();
        }
        SandSagaScenario scenario = configProvider.scenario(input.scenario);
        if (scenario == null) {
            throw new BadRequestException();
        }

        if (!limitEntity.next()) {
            LOGGER.warn("API limit exceeded - completed entity");
            throw new ApiLimitExceeded();
        }

        CompletedEntity entity = new CompletedEntity();
        entity.time = System.currentTimeMillis();
        entity.scenarioId = scenario.getEntityId();
        entity.ip = ip;
        entity.userId = UserEntity.findByPrincipalAsId(userPrincipal);

        if (scenario.getStoreSnapshot()) {
            if (limitAdditionalData.next()) {
                if (input.metadata != null && input.metadata.length() <= 8192) {
                    entity.metadata = input.metadata;
                }
                if (input.data != null && input.data.length <= 150_000) {
                    entity.snapshot = input.data;
                }
            } else {
                LOGGER.warn("API limit exceeded - completed additional data");
            }
        }

        liveStatsProvider.incrementCompleted();

        CompletedEntity.persist(entity);
        return entity.id;
    }

    @Transactional
    public OutCompletedDto get(Long id) {
        CompletedEntity entity = CompletedEntity.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        return asDto(entity);
    }

    @Transactional
    public byte[] getSnapshotData(Long id) {
        CompletedEntity entity = CompletedEntity.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        return entity.snapshot;
    }

    @Transactional
    public void deleteSnapshotData(Long id) {
        CompletedEntity entity = CompletedEntity.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.snapshot = null;
        entity.persist();
    }

    @Transactional
    public List<OutCompletedDto> list(int pageIndex, int pageSize) {
        return CompletedEntity.<CompletedEntity>findAll(Sort.by("time").descending())
                .page(pageIndex, pageSize)
                .stream().map(this::asDto).collect(Collectors.toList());
    }

    private OutCompletedDto asDto(CompletedEntity e) {
        OutCompletedDto dto = new OutCompletedDto();
        dto.setId(e.id);
        dto.setTime(e.time);
        if (e.scenarioId != null) {
            SandSagaScenario scenario = configProvider.scenario(e.scenarioId);
            if (scenario != null) {
                dto.setScenario(scenario.getName());
            }
        }
        dto.setMetadata(e.metadata);
        dto.setSnapshotSize((e.snapshot == null) ? 0 : e.snapshot.length);
        dto.setIp(e.ip);
        dto.setUserId(e.userId);
        return dto;
    }

    @Transactional
    public void delete(Long id) {
        boolean b = CompletedEntity.deleteById(id);
        if (!b) {
            throw new NotFoundException();
        }
    }

    @Transactional
    public Map<String, Boolean> completedScenarios(long userId) {
        // freemarker not supports sets
        Map<String, Boolean> set = new LinkedHashMap<>();

        List<Long> scenarios = CompletedEntity.collectCompletedScenarios(userId);
        for (Long scenarioId : scenarios) {
            SandSagaScenario scenario = configProvider.scenario(scenarioId);
            if (scenario != null) {
                set.put(scenario.getName(), true);
            }
        }

        return set;
    }

    @Transactional
    public OutClaimDto claim(InClaimDto claimData, String ip, Principal userPrincipal) {
        Set<OutClaimEntryDto> accepted = new LinkedHashSet<>();
        Set<OutClaimEntryDto> refused = new LinkedHashSet<>();

        Long userId = UserEntity.findByPrincipalAsId(userPrincipal);
        if (userId == null) {
            throw new ForbiddenException("User not found");
        }

        if (claimData != null && claimData.getCompleted() != null) {
            for (InClaimEntryDto entry : claimData.getCompleted()) {
                if (entry == null || entry.getId() == null || entry.getScenario() == null) {
                    // missing information
                    continue;
                }

                CompletedEntity completed = CompletedEntity.findById(entry.getId());
                if (validateClaim(entry, completed, ip)) {
                    completed.userId = userId;
                    completed.persist();

                    accepted.add(OutClaimEntryDto.convert(entry));

                    LOGGER.info("Claim accepted - user id: " + userId + ", completed id: " + entry.getId());

                } else {
                    refused.add(OutClaimEntryDto.convert(entry));

                    LOGGER.info("Claim refused - user id: " + userId + ", completed id: " + entry.getId());
                }
            }
        }

        OutClaimDto dto = new OutClaimDto();
        dto.setAccepted(accepted);
        dto.setRefused(refused);
        return dto;
    }

    private boolean validateClaim(InClaimEntryDto entry, CompletedEntity completed, String ip) {
        if (completed == null) {
            // not found
            return false;
        }

        if (completed.userId != null) {
            // already claimed
            return false;
        }

//        if (!Objects.equals(completed.ip, ip)) {
//            // ip not matches
//            return false;
//        }

        String entryScenario = entry.getScenario();
        SandSagaScenario scenario = configProvider.scenario(entryScenario);
        if (scenario == null) {
            // scenario not found
            return false;
        }
        if (!Objects.equals(entryScenario, scenario.getName())) {
            // scenario not matches
            return false;
        }

        return true;
    }

    public double getLimitEntityUsedRatio() {
        return limitEntity.countUsedRatio();
    }

    public double getLimitAdditionalDataUsedRatio() {
        return limitAdditionalData.countUsedRatio();
    }
}