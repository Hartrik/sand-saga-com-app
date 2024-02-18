package cz.harag.sandsaga.web.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author Patrik Harag
 * @version 2024-02-18
 */
@Entity
@Table(name = "t_day_stats")
public class DayStatsEntity extends PanacheEntityBase {

    @Id
    public Long id;

    @Column(nullable = false)
    public Long updates;

    public static int updateDayStats(Long id) {
        return update("updates = updates + 1 WHERE id = ?1", id);
    }

    public static long sumUpdates() {
        SumUpdatesDto result = DayStatsEntity.find("SELECT SUM(updates) FROM DayStatsEntity")
                .project(SumUpdatesDto.class).singleResult();
        if (result == null || result.updates == null) {
            return 0;
        }
        return result.updates;
    }


    @RegisterForReflection
    private static final class SumUpdatesDto {
        public final Long updates;

        public SumUpdatesDto(Long updates){
            this.updates = updates;
        }
    }
}