package cz.harag.sandsaga.web.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author Patrik Harag
 * @version 2024-03-02
 */
@RegisterForReflection
public class OutUserDto {

    private long id;
    private long timeRegistered;
    private String email;
    private String displayName;
    private String role;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimeRegistered() {
        return timeRegistered;
    }

    public void setTimeRegistered(long timeRegistered) {
        this.timeRegistered = timeRegistered;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}