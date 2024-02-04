package cz.harag.sandsaga.web.service;

import java.util.List;
import java.util.stream.Collectors;

import cz.harag.sandsaga.web.dto.ReportDto;
import cz.harag.sandsaga.web.dto.MultipartReport;
import cz.harag.sandsaga.web.model.Report;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

/**
 * @author Patrik Harag
 * @version 2024-02-04
 */
@ApplicationScoped
public class ReportProvider {

    private static final Logger LOGGER = Logger.getLogger(ReportProvider.class);

    @Transactional
    public Long report(MultipartReport input, String ip) {
        LOGGER.info("Storing report from IP: " + ip);

        Report report = new Report();
        report.time = System.currentTimeMillis();
        report.ip = ip;
        report.scenario = input.scenario;
        report.location = input.location;
        report.message = input.message;
        report.metadata = input.metadata;

        Report.persist(report);
        return report.id;
    }

    @Transactional
    public List<ReportDto> list() {
        return Report.<Report>listAll().stream().map(e -> {
            ReportDto dto = new ReportDto();
            dto.setId(e.id);
            dto.setTime(e.time);
            dto.setScenario(e.scenario);
            dto.setMessage(e.message);
            dto.setLocation(e.location);
            dto.setMetadata(e.metadata);
            dto.setIp(e.ip);
            return dto;
        }).collect(Collectors.toList());
    }
}