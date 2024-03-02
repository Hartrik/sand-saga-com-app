package cz.harag.sandsaga.web.security;

import java.security.Principal;

/**
 * @author Patrik Harag
 * @version 2024-03-02
 */
public class DiscordPrincipal implements Principal {
    private final String username;
    private final String email;
    private final String id;

    public DiscordPrincipal(String username, String email, String id) {
        this.username = username;
        this.email = email;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return username;
    }
}
