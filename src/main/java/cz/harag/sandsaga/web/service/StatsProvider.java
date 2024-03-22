package cz.harag.sandsaga.web.service;

import cz.harag.sandsaga.web.dto.OutStatsDayDto;
import cz.harag.sandsaga.web.dto.OutStatsDto;
import cz.harag.sandsaga.web.dto.OutStatsScenarioDto;
import cz.harag.sandsaga.web.model.CompletedEntity;
import cz.harag.sandsaga.web.model.DayStatsEntity;
import cz.harag.sandsaga.web.model.ScenarioEntity;
import io.quarkus.panache.common.Sort;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Patrik Harag
 * @version 2024-03-22
 */
@ApplicationScoped
public class StatsProvider {

    @Transactional
    public List<OutStatsScenarioDto> listScenarioStats(int pageIndex, int pageSize) {
        return ScenarioEntity.<ScenarioEntity>findAll(Sort.by("id").ascending())
                .page(pageIndex, pageSize)
                .stream().map(this::asDto).collect(Collectors.toList());
    }

    @Transactional
    public OutStatsDto sumScenarioStats() {
        return ScenarioEntity.sumStats();
    }

    @Transactional
    public List<OutStatsDayDto> listDayStats(int pageIndex, int pageSize) {
        return DayStatsEntity.<DayStatsEntity>findAll(Sort.by("id").descending())
                .page(pageIndex, pageSize)
                .stream().map(this::asDto).collect(Collectors.toList());
    }

    @Transactional
    public OutStatsDto sumDayStats() {
        return DayStatsEntity.sumStats();
    }

    private OutStatsDayDto asDto(DayStatsEntity e) {
        OutStatsDayDto dto = new OutStatsDayDto();
        dto.setId(e.id);
        dto.setUpdates(e.updates);
        dto.setCompleted(e.completed);
        return dto;
    }

    private OutStatsScenarioDto asDto(ScenarioEntity e) {
        OutStatsScenarioDto dto = new OutStatsScenarioDto();
        dto.setId(e.id);
        dto.setName(e.name);
        dto.setUpdates(e.updates);
        dto.setCompleted(e.completed);
        return dto;
    }


    // TEMPORARY

    @Transactional
    @Scheduled(every = "15m", delayed = "15m")
    public void updateStatsFromCompleted() {
        for (ScenarioEntity scenario : ScenarioEntity.<ScenarioEntity>listAll()) {
            scenario.completed = CompletedEntity.countCompleted(scenario);
            scenario.persist();
        }
        for (DayStatsEntity dayStats : DayStatsEntity.<DayStatsEntity>listAll()) {
            dayStats.completed = CompletedEntity.countCompleted(dayStats.id);
            dayStats.persist();
        }
    }
}