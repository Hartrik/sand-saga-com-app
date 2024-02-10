package cz.harag.sandsaga.web.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * @author Patrik Harag
 * @version 2024-02-10
 */
@Entity
@Table(name = "t_scenario")
public class ScenarioEntity extends PanacheEntity {

    // PK is defined in PanacheEntity

    @Column(nullable = false, length = 32)
    public String name;


    public static PanacheQuery<ScenarioEntity> findByName(String name) {
        return ScenarioEntity.find("name", name);
    }
}