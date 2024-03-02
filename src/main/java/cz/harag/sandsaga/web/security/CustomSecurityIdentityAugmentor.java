package cz.harag.sandsaga.web.security;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.oidc.UserInfo;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;

/**
 * @author Patrik Harag
 * @version 2024-03-02
 */
@ApplicationScoped
public class CustomSecurityIdentityAugmentor implements SecurityIdentityAugmentor {

    @Override
    public Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context) {
		if (identity.isAnonymous()) {
			// not logged in
			return Uni.createFrom().item(() -> identity);
		}

		UserInfo userInfo = identity.getAttribute("userinfo");
		if (userInfo == null) {
			// logged using JPA Security / form based
			QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder(identity);
			builder.setPrincipal(new FormPrincipal(identity.getPrincipal().getName()));
			return Uni.createFrom().item(builder.build());
		}

		// logged using OIDC
		QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder(identity);
		String username = userInfo.getString("username");
		if (username == null) {
			username = "";
		}
		String tenantId = identity.getAttribute("tenant-id");
		String id = userInfo.getString("id");
		if (id == null) {
			throw new RuntimeException("UserInfo / id cannot be null");
		}
		String email = userInfo.getString("email");
		builder.setPrincipal(new DiscordPrincipal(username, email, id));
		builder.addRole("user");
        return Uni.createFrom().item(builder.build());
    }
}