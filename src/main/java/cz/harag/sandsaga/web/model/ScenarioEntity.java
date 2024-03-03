package cz.harag.sandsaga.web.model;

import cz.harag.sandsaga.web.dto.OutStatsDto;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * @author Patrik Harag
 * @version 2024-02-24
 */
@Entity
@Table(name = "t_scenario")
public class ScenarioEntity extends PanacheEntity {

    // PK is defined in PanacheEntity

    @Column(nullable = false, length = 32)
    public String name;

    @Column
    public Long updates = 0L;

    @Column
    public Long completed = 0L;


    public static PanacheQuery<ScenarioEntity> findByName(String name) {
        return ScenarioEntity.find("name", name);
    }

    public static int incrementUpdates(Long id) {
        return update("updates = updates + 1 WHERE id = ?1", id);
    }

    public static OutStatsDto sumStats() {
        StatsSumDto result = ScenarioEntity.find("SELECT SUM(updates), SUM(completed) FROM ScenarioEntity")
                .project(StatsSumDto.class).singleResult();

        OutStatsDto stats = new OutStatsDto();
        if (result != null) {
            if (result.updates != null) {
                stats.setUpdates(result.updates);
            }
            if (result.completed != null) {
                stats.setCompleted(result.completed);
            }
        }
        return stats;
    }

    /**
     * @author Patrik Harag
     * @version 2024-02-24
     */
    @RegisterForReflection
    private static final class StatsSumDto {
        public final Long updates;
        public final Long completed;

        public StatsSumDto(Long updates, Long completed) {
            this.updates = updates;
            this.completed = completed;
        }
    }
}