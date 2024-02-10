package cz.harag.sandsaga.web.model;

import java.security.Principal;

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
 * @version 2022-10-04
 */
@Entity
@Table(name = "t_user")
@UserDefinition 
public class UserEntity extends PanacheEntity {

    public static final String ROLE_ADMIN = "admin";


    @Username
    @Column(nullable = false, unique = true)
    public String username;

    @Password
    public String password;

    @Roles
    public String role;


    public static UserEntity asUser(Principal principal) {
        if (principal == null) {
            return null;
        }
        return UserEntity.find("username", principal.getName()).firstResult();
    }
}