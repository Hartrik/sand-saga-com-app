package cz.harag.sandsaga.web.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author Patrik Harag
 * @version 2024-03-02
 */
@RegisterForReflection
public class OutCompletedDto {

    private long id;
    private long time;
    private String scenario;
    private String metadata;
    private long snapshotSize;
    private String ip;
    private Long userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public long getSnapshotSize() {
        return snapshotSize;
    }

    public void setSnapshotSize(long snapshotSize) {
        this.snapshotSize = snapshotSize;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}