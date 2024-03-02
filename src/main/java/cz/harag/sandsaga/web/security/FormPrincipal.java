package cz.harag.sandsaga.web.security;

import java.security.Principal;

/**
 * @author Patrik Harag
 * @version 2024-03-02
 */
public class FormPrincipal implements Principal {
    private final String username;

    public FormPrincipal(String username) {
        this.username = username;
    }

    @Override
    public String getName() {
        return username;
    }
}
