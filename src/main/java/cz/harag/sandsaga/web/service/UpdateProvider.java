package cz.harag.sandsaga.web.service;

import cz.harag.sandsaga.web.dto.InUpdateMultipart;
import cz.harag.sandsaga.web.dto.SandSagaScenario;
import cz.harag.sandsaga.web.model.DayStatsEntity;
import cz.harag.sandsaga.web.model.ScenarioEntity;
import io.quarkus.runtime.Shutdown;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jboss.logging.Logger;

/**
 *
 * @author Patrik Harag
 * @version 2024-03-22
 */
@ApplicationScoped
public class UpdateProvider {

    private static final Logger LOGGER = Logger.getLogger(UpdateProvider.class);

    @Inject
    SandSagaConfigProvider configProvider;

    @Inject
    LiveStatsProvider liveStatsProvider;

    private final List<InUpdateMultipart> toProcess = new ArrayList<>();

    public void update(InUpdateMultipart input, String ip) {
        synchronized (this) {
            toProcess.add(input);
        }
        liveStatsProvider.incrementUpdates();
    }

    // it stores updates in batches

    @Transactional
    @Scheduled(every = "1m", delayed = "15s")
    public void flushStats() {
        List<InUpdateMultipart> todo = null;
        synchronized (this) {
            if (!this.toProcess.isEmpty()) {
                todo = new ArrayList<>(this.toProcess);
                this.toProcess.clear();
            }
        }

        if (todo != null) {
            long totalUpdates = 0;  // valid only

            // increment scenario stats
            for (Map.Entry<String, Long> entry : todo.stream()
                    .filter(input -> input.scenario != null)
                    .collect(Collectors.groupingBy(input -> input.scenario, Collectors.counting())).entrySet()) {

                String scenario = entry.getKey();
                long scenarioUpdates = entry.getValue();
                SandSagaScenario scenarioObject = configProvider.scenario(scenario);
                if (scenarioObject != null) {
                    ScenarioEntity.incrementUpdates(scenarioObject.getEntityId(), scenarioUpdates);
                    totalUpdates += scenarioUpdates;
                }
            }

            // increment day stats
            Long epochDay = System.currentTimeMillis() / 86_400_000;
            if (DayStatsEntity.incrementUpdates(epochDay, totalUpdates) <= 0) {
                // create entity
                DayStatsEntity entity = new DayStatsEntity();
                entity.id = epochDay;
                entity.updates = totalUpdates;
                entity.persistAndFlush();
            }
        }
    }

    @Shutdown
    void onShutdown() {
        LOGGER.info("Flushing on shutdown...");
        flushStats();
    }
}