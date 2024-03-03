package cz.harag.sandsaga.web.controller;

import java.net.URI;
import java.net.URISyntaxException;

import io.quarkus.oidc.OidcSession;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

/**
 * @author Patrik Harag
 * @version 2024-03-03
 */
@Path("/")
@RolesAllowed({"user", "admin"})
public class LoggedController {

    @Inject
    OidcSession oidcSession;

    @GET
    @Path("/logout")
    public Response logout() throws URISyntaxException {
        oidcSession.logout().await().indefinitely();
        return Response.seeOther(new URI("/")).build();
    }
}
