package cz.harag.sandsaga.web.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * @author Patrik Harag
 * @version 2024-02-18
 */
@Entity
@Table(name = "t_scenario")
public class ScenarioEntity extends PanacheEntity {

    // PK is defined in PanacheEntity

    @Column(nullable = false, length = 32)
    public String name;

    @Column
    public Long updates = 0L;


    public static PanacheQuery<ScenarioEntity> findByName(String name) {
        return ScenarioEntity.find("name", name);
    }

    public static int incrementUpdates(Long id) {
        return update("updates = updates + 1 WHERE id = ?1", id);
    }
}