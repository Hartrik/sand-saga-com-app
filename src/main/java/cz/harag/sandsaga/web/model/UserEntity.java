package cz.harag.sandsaga.web.model;

import java.security.Principal;

import cz.harag.sandsaga.web.security.DiscordPrincipal;
import cz.harag.sandsaga.web.security.FormPrincipal;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * @author Patrik Harag
 * @version 2024-03-02
 */
@Entity
@Table(name = "t_user")
@UserDefinition 
public class UserEntity extends PanacheEntity {

    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_USER = "user";


    // form based auth

    @Username
    @Column(unique = true)
    public String username;

    @Password
    public String password;

    // oidc discord auth

    @Column(unique = true)
    public String discordId;

    // ---

    @Column
    public Long timeRegistered;

    @Roles
    public String role = ROLE_USER;

    @Column
    public String email;

    @Column
    public String displayName;

    // ---

    public static UserEntity findByPrincipal(Principal principal) {
        if (principal instanceof DiscordPrincipal discordPrincipal) {
            return findByDiscordId(discordPrincipal.getId());
        }
        if (principal instanceof FormPrincipal formPrincipal) {
            return findByUsername(formPrincipal.getName());
        }
        return null;
    }

    public static UserEntity findByUsername(String username) {
        return UserEntity.find("username", username).firstResult();
    }

    public static UserEntity findByDiscordId(String id) {
        return UserEntity.find("discordId", id).firstResult();
    }
}