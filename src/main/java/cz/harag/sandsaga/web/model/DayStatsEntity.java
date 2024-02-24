package cz.harag.sandsaga.web.model;

import cz.harag.sandsaga.web.dto.StatsDto;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author Patrik Harag
 * @version 2024-02-24
 */
@Entity
@Table(name = "t_day_stats")
public class DayStatsEntity extends PanacheEntityBase {

    @Id
    public Long id;

    @Column(nullable = false)
    public Long updates = 0L;

    @Column
    public Long completed = 0L;

    public static int incrementUpdates(Long id) {
        return update("updates = updates + 1 WHERE id = ?1", id);
    }

    public static StatsDto sumStats() {
        StatsSumDto result = DayStatsEntity.find("SELECT SUM(updates), SUM(completed) FROM DayStatsEntity")
                .project(StatsSumDto.class).singleResult();

        StatsDto stats = new StatsDto();
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