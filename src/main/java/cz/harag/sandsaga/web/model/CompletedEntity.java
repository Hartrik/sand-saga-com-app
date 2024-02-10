package cz.harag.sandsaga.web.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * @author Patrik Harag
 * @version 2024-02-10
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
}