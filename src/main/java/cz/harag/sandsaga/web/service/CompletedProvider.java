package cz.harag.sandsaga.web.service;

import java.util.List;
import java.util.stream.Collectors;

import cz.harag.sandsaga.web.dto.CompletedDto;
import cz.harag.sandsaga.web.dto.MultipartCompleted;
import cz.harag.sandsaga.web.dto.SandSagaScenario;
import cz.harag.sandsaga.web.model.CompletedEntity;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.jboss.logging.Logger;

/**
 * @author Patrik Harag
 * @version 2024-02-18
 */
@ApplicationScoped
public class CompletedProvider {

    private static final Logger LOGGER = Logger.getLogger(CompletedProvider.class);

    private final ApiLimit limitEntity = new ApiLimit(ApiLimit.EPOCH_DURATION_DAY, 10_000);
    private final ApiLimit limitAdditionalData = new ApiLimit(ApiLimit.EPOCH_DURATION_DAY, 100);

    @Inject
    SandSagaConfigProvider configProvider;

    @Transactional
    public Long store(MultipartCompleted input, String ip) {
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
        entity.ip = ip;
        entity.scenarioId = scenario.getEntityId();;

        if (limitAdditionalData.next()) {
            if (input.metadata != null && input.metadata.length() <= 8192) {
                entity.metadata = input.metadata;
            }

            if (scenario.getStoreSnapshot()) {
                if (input.data != null && input.data.length <= 150_000) {
                    entity.snapshot = input.data;
                }
            }
        } else {
            LOGGER.warn("API limit exceeded - completed additional data");
        }

        CompletedEntity.persist(entity);
        return entity.id;
    }

    @Transactional
    public CompletedDto get(Long id) {
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
    public List<CompletedDto> list(int pageIndex, int pageSize) {
        return CompletedEntity.<CompletedEntity>findAll(Sort.by("time").descending())
                .page(pageIndex, pageSize)
                .stream().map(this::asDto).collect(Collectors.toList());
    }

    private CompletedDto asDto(CompletedEntity e) {
        CompletedDto dto = new CompletedDto();
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
        return dto;
    }

    @Transactional
    public void delete(Long id) {
        boolean b = CompletedEntity.deleteById(id);
        if (!b) {
            throw new NotFoundException();
        }
    }

    public double getLimitEntityUsedRatio() {
        return limitEntity.countUsedRatio();
    }

    public double getLimitAdditionalDataUsedRatio() {
        return limitAdditionalData.countUsedRatio();
    }
}