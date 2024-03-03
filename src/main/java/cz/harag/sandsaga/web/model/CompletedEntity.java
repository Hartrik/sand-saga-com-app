package cz.harag.sandsaga.web.model;

import java.util.List;
import java.util.stream.Collectors;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * @author Patrik Harag
 * @version 2024-03-03
 */
@Entity
@Table(name = "t_completed")
public class CompletedEntity extends PanacheEntity {

    // PK is defined in PanacheEntity

    @Column(name = "scenario_id", nullable = false)
    public Long scenarioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scenario_id", referencedColumnName = "id", insertable = false, updatable = false)
    public ScenarioEntity scenario;

    @Column(nullable = false)
    public Long time;

    @Column(nullable = true, length = 8192)
    public String metadata;

    @Column(nullable = true, length = 150_000)
    public byte[] snapshot;

    @Column(nullable = false, length = 48)
    public String ip;

    @Column(name = "user_id", nullable = true)
    public Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    public UserEntity user;


    public static long countCompleted(ScenarioEntity scenarioEntity) {
        return CompletedEntity.count("scenario = ?1", scenarioEntity);
    }

    public static long countCompleted(long epochDay) {
        long start = epochDay * 86_400_000;
        long end = start + 86_400_000;
        return CompletedEntity.count("time > ?1 and time < ?2", start, end);
    }

    public static List<Long> collectCompletedScenarios(long userId) {
        return CompletedEntity.find("SELECT DISTINCT scenario.id FROM CompletedEntity WHERE user.id = ?1", userId)
                .project(CollectCompletedScenariosRowType.class)
                .stream()
                .map(s -> s.scenarioId)
                .collect(Collectors.toList());
    }

    /**
     * @author Patrik Harag
     * @version 2024-03-03
     */
    @RegisterForReflection
    private static final class CollectCompletedScenariosRowType {
        public final Long scenarioId;

        public CollectCompletedScenariosRowType(Long scenarioId) {
            this.scenarioId = scenarioId;
        }
    }
}