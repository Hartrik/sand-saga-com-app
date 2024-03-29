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
 * @version 2024-03-02
 */
@Entity
@Table(name = "t_report")
public class ReportEntity extends PanacheEntity {

    // PK is defined in PanacheEntity

    @Column(nullable = false)
    public Long time;

    @Column(name = "scenario_id", nullable = true)
    public Long scenarioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scenario_id", referencedColumnName = "id", insertable = false, updatable = false)
    public ScenarioEntity scenario;

    @Column(nullable = false, length = 32)
    public String location;

    @Column(nullable = false, length = 1024)
    public String message;

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
}