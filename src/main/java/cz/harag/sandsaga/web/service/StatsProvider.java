package cz.harag.sandsaga.web.service;

import java.util.List;
import java.util.stream.Collectors;

import cz.harag.sandsaga.web.dto.StatsDayDto;
import cz.harag.sandsaga.web.dto.MultipartUpdate;
import cz.harag.sandsaga.web.dto.SandSagaScenario;
import cz.harag.sandsaga.web.dto.StatsDto;
import cz.harag.sandsaga.web.dto.StatsScenarioDto;
import cz.harag.sandsaga.web.model.CompletedEntity;
import cz.harag.sandsaga.web.model.DayStatsEntity;
import cz.harag.sandsaga.web.model.ScenarioEntity;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

/**
 * @author Patrik Harag
 * @version 2024-02-24
 */
@ApplicationScoped
public class StatsProvider {

    private static final Logger LOGGER = Logger.getLogger(StatsProvider.class);

    @Inject
    SandSagaConfigProvider configProvider;

    @Transactional
    public void update(MultipartUpdate input, String ip) {
        Long epochDay = System.currentTimeMillis() / 86_400_000;

        // increment day stats
        if (DayStatsEntity.incrementUpdates(epochDay) > 0) {
            // ok

        } else {
            // create entity
            DayStatsEntity entity = new DayStatsEntity();
            entity.id = epochDay;
            entity.updates = 1L;
            entity.persistAndFlush();
        }

        // increment scenario stats
        if (input.scenario != null) {
            SandSagaScenario scenario = configProvider.scenario(input.scenario);
            if (scenario != null) {
                ScenarioEntity.incrementUpdates(scenario.getEntityId());
            }
        }
    }

    @Transactional
    public List<StatsScenarioDto> listScenarioStats(int pageIndex, int pageSize) {
        return ScenarioEntity.<ScenarioEntity>findAll(Sort.by("id").ascending())
                .page(pageIndex, pageSize)
                .stream().map(this::asDto).collect(Collectors.toList());
    }

    @Transactional
    public StatsDto sumScenarioStats() {
        return ScenarioEntity.sumStats();
    }

    @Transactional
    public List<StatsDayDto> listDayStats(int pageIndex, int pageSize) {
        return DayStatsEntity.<DayStatsEntity>findAll(Sort.by("id").descending())
                .page(pageIndex, pageSize)
                .stream().map(this::asDto).collect(Collectors.toList());
    }

    @Transactional
    public StatsDto sumDayStats() {
        return DayStatsEntity.sumStats();
    }

    private StatsDayDto asDto(DayStatsEntity e) {
        StatsDayDto dto = new StatsDayDto();
        dto.setId(e.id);
        dto.setUpdates(e.updates);
        dto.setCompleted(e.completed);
        return dto;
    }

    private StatsScenarioDto asDto(ScenarioEntity e) {
        StatsScenarioDto dto = new StatsScenarioDto();
        dto.setId(e.id);
        dto.setName(e.name);
        dto.setUpdates(e.updates);
        dto.setCompleted(e.completed);
        return dto;
    }


    // TEMPORARY

    @Transactional
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