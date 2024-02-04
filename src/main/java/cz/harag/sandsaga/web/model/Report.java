package cz.harag.sandsaga.web.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * @author Patrik Harag
 * @version 2024-02-04
 */
@Entity
@Table(name = "t_report")
public class Report extends PanacheEntity {

    // PK is defined in PanacheEntity

    @Column(nullable = false)
    public Long time;

    @Column(nullable = false, length = 32)
    public String scenario;

    @Column(nullable = false, length = 32)
    public String location;

    @Column(nullable = false, length = 1024)
    public String message;

    @Column(nullable = true, length = 8192)
    public String metadata;

    @Column(nullable = false, length = 48)
    public String ip;
}