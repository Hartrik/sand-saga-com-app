package cz.harag.sandsaga.web.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author Patrik Harag
 * @version 2024-02-18
 */
@RegisterForReflection
public class StatsDayDto {

    private long id;
    private long updates;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUpdates() {
        return updates;
    }

    public void setUpdates(long updates) {
        this.updates = updates;
    }
}