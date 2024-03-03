package cz.harag.sandsaga.web.service;

import cz.harag.sandsaga.web.dto.OutStatsDto;
import cz.harag.sandsaga.web.model.CompletedEntity;
import cz.harag.sandsaga.web.model.ScenarioEntity;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;

/**
 * Fast stats - can be used on the front page.
 *
 * @author Patrik Harag
 * @version 2024-03-02
 */
@ApplicationScoped
public class LiveStatsProvider {

    private long completed = 0;
    private long updates = 0;

    @Transactional
    synchronized void onStart(@Observes StartupEvent event) {
        completed = CompletedEntity.count();
        updates = ScenarioEntity.sumStats().getUpdates();
    }

    synchronized void incrementCompleted() {
        completed++;
    }

    synchronized void incrementUpdates() {
        updates++;
    }

    public synchronized OutStatsDto getStats() {
        OutStatsDto statsDto = new OutStatsDto();
        statsDto.setCompleted(completed);
        statsDto.setUpdates(updates);
        return statsDto;
    }
}