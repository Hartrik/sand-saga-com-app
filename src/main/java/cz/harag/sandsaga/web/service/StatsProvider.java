package cz.harag.sandsaga.web.service;

import java.util.List;
import java.util.stream.Collectors;

import cz.harag.sandsaga.web.dto.DayStatsDto;
import cz.harag.sandsaga.web.dto.MultipartUpdate;
import cz.harag.sandsaga.web.dto.StatsDto;
import cz.harag.sandsaga.web.model.DayStatsEntity;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

/**
 * @author Patrik Harag
 * @version 2024-02-18
 */
@ApplicationScoped
public class StatsProvider {

    private static final Logger LOGGER = Logger.getLogger(StatsProvider.class);


    @Transactional
    public void update(MultipartUpdate multipart, String ip) {
        Long epochDay = System.currentTimeMillis() / 86_400_000;

        if (DayStatsEntity.updateDayStats(epochDay) > 0) {
            // ok

        } else {
            // create entity
            DayStatsEntity entity = new DayStatsEntity();
            entity.id = epochDay;
            entity.updates = 1L;
            entity.persistAndFlush();
        }
    }

    @Transactional
    public List<DayStatsDto> list(int pageIndex, int pageSize) {
        return DayStatsEntity.<DayStatsEntity>findAll(Sort.by("id").descending())
                .page(pageIndex, pageSize)
                .stream().map(this::asDto).collect(Collectors.toList());
    }

    @Transactional
    public StatsDto sum() {
        StatsDto dto = new StatsDto();
        dto.setUpdates(DayStatsEntity.sumUpdates());
        return dto;
    }

    private DayStatsDto asDto(DayStatsEntity e) {
        DayStatsDto dto = new DayStatsDto();
        dto.setId(e.id);
        dto.setUpdates(e.updates);
        return dto;
    }
}