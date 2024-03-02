package cz.harag.sandsaga.web.security;

import cz.harag.sandsaga.web.model.UserEntity;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.spi.runtime.SecurityEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.transaction.Transactional;
import java.security.Principal;
import org.jboss.logging.Logger;

/**
 * @author Patrik Harag
 * @version 2024-03-02
 */
@ApplicationScoped
public class CustomSecurityEventObserver {

    private static final Logger LOG = Logger.getLogger(CustomSecurityEventObserver.class);

    @Transactional
    public void event(@ObservesAsync SecurityEvent event) {
        if (event instanceof io.quarkus.oidc.SecurityEvent) {
            if (((io.quarkus.oidc.SecurityEvent) event).getEventType() == io.quarkus.oidc.SecurityEvent.Type.OIDC_LOGIN) {
                SecurityIdentity securityIdentity = event.getSecurityIdentity();
                Principal principal = securityIdentity.getPrincipal();
                if (principal instanceof DiscordPrincipal discordPrincipal) {
                    createIfNotExists(discordPrincipal);
                }
            }
        }
    }

    private void createIfNotExists(DiscordPrincipal discordPrincipal) {
        UserEntity user = UserEntity.findByDiscordId(discordPrincipal.getId());
        if (user == null) {
            // create new entry
            user = new UserEntity();
            user.timeRegistered = System.currentTimeMillis();
            user.discordId = discordPrincipal.getId();
            user.displayName = discordPrincipal.getName();
            user.email = discordPrincipal.getEmail();
            user.persist();

            LOG.info("User registered: " + user.id + " / discord " + user.discordId + " / " + user.email);
        }
    }
}