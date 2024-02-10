package cz.harag.sandsaga.web.service;

import cz.harag.sandsaga.web.model.UserEntity;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

/**
 * @author Patrik Harag
 * @version 2024-01-20
 */
@ApplicationScoped
public class DatabasePopulator {

    private static final Logger LOGGER = Logger.getLogger(DatabasePopulator.class);

    @ConfigProperty(name = "cz.harag.sandsaga.web.database-populator.admin.login")
    String adminLogin;

    @ConfigProperty(name = "cz.harag.sandsaga.web.database-populator.admin.password")
    String adminPassword;

    @Transactional
    void onStart(@Observes StartupEvent event) {
        if (UserEntity.count() == 0) {
            UserEntity user = new UserEntity();
            user.username = adminLogin;
            user.password = adminPassword;
            user.role = UserEntity.ROLE_ADMIN;

            user.persist();

            LOGGER.info("User added: " + adminLogin);
        }
    }

    //    public static void main(String[] args) {
    //        String password = "password1";
    //        System.out.println(io.quarkus.elytron.security.common.BcryptUtil.bcryptHash(password));
    //    }
}