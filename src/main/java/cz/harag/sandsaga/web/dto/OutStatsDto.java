package cz.harag.sandsaga.web.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author Patrik Harag
 * @version 2024-02-24
 */
@RegisterForReflection
public class OutStatsDto {

    private long updates;
    private long completed;

    public long getUpdates() {
        return updates;
    }

    public void setUpdates(long updates) {
        this.updates = updates;
    }

    public long getCompleted() {
        return completed;
    }

    public void setCompleted(long completed) {
        this.completed = completed;
    }
}