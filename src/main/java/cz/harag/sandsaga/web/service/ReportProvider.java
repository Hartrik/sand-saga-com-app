package cz.harag.sandsaga.web.service;

import java.util.List;
import java.util.stream.Collectors;

import cz.harag.sandsaga.web.dto.ReportDto;
import cz.harag.sandsaga.web.dto.MultipartReport;
import cz.harag.sandsaga.web.dto.SandSagaScenario;
import cz.harag.sandsaga.web.model.ReportEntity;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.jboss.logging.Logger;

/**
 * @author Patrik Harag
 * @version 2024-02-10
 */
@ApplicationScoped
public class ReportProvider {

    private static final Logger LOGGER = Logger.getLogger(ReportProvider.class);

    private final ApiLimit reportLimit = new ApiLimit(ApiLimit.EPOCH_DURATION_DAY, 100);

    @Inject
    SandSagaConfigProvider configProvider;

    @Transactional
    public Long report(MultipartReport input, String ip) {
        LOGGER.info("Storing report from IP: " + ip);

        // basic validation
        if (input.location == null) {
            throw new BadRequestException();
        }
        if (input.message == null) {
            throw new BadRequestException();
        }

        if (!reportLimit.next()) {
            LOGGER.info("API limit exceeded - report");
            throw new ApiLimitExceeded();
        }

        ReportEntity report = new ReportEntity();
        report.time = System.currentTimeMillis();
        report.ip = ip;
        report.location = input.location;
        report.message = input.message;

        if (input.metadata != null && input.metadata.length() <= 8192) {
            report.metadata = input.metadata;
        }

        if (input.data != null && input.data.length <= 150_000) {
            report.snapshot = input.data;
        }

        if (input.scenario != null) {
            SandSagaScenario scenario = configProvider.scenario(input.scenario);
            if (scenario != null) {
                report.scenarioId = scenario.getEntityId();
            }
        }

        ReportEntity.persist(report);
        return report.id;
    }

    @Transactional
    public ReportDto get(Long id) {
        ReportEntity entity = ReportEntity.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        return asDto(entity);
    }

    @Transactional
    public byte[] getSnapshotData(Long id) {
        ReportEntity entity = ReportEntity.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        return entity.snapshot;
    }

    @Transactional
    public List<ReportDto> list(int pageIndex, int pageSize) {
        return ReportEntity.<ReportEntity>findAll(Sort.by("time").descending())
                .page(pageIndex, pageSize)
                .stream().map(this::asDto).collect(Collectors.toList());
    }

    private ReportDto asDto(ReportEntity e) {
        ReportDto dto = new ReportDto();
        dto.setId(e.id);
        dto.setTime(e.time);
        if (e.scenarioId != null) {
            SandSagaScenario scenario = configProvider.scenario(e.scenarioId);
            if (scenario != null) {
                dto.setScenario(scenario.getName());
            }
        }
        dto.setMessage(e.message);
        dto.setLocation(e.location);
        dto.setMetadata(e.metadata);
        dto.setSnapshotSize((e.snapshot == null) ? 0 : e.snapshot.length);
        dto.setIp(e.ip);
        return dto;
    }

    @Transactional
    public void delete(Long id) {
        boolean b = ReportEntity.deleteById(id);
        if (!b) {
            throw new NotFoundException();
        }
    }

    public double getApiLimitUsedRatio() {
        return reportLimit.countUsedRatio();
    }
}