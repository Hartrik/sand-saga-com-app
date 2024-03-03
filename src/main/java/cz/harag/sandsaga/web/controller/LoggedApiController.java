package cz.harag.sandsaga.web.controller;

import cz.harag.sandsaga.web.dto.InClaimDto;
import cz.harag.sandsaga.web.dto.OutClaimDto;
import cz.harag.sandsaga.web.service.CompletedProvider;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import org.jboss.resteasy.spi.HttpRequest;

/**
 * @author Patrik Harag
 * @version 2024-03-03
 */
@Path("/api/user")
@RolesAllowed({"user", "admin"})
public class LoggedApiController {

    @Context
    SecurityContext security;

    @Inject
    CompletedProvider completedProvider;

    @POST
    @Path("/claim-completed")
    @Produces(MediaType.APPLICATION_JSON)
    public OutClaimDto handleClaimCompleted(InClaimDto claimData, @Context HttpRequest request) {
        String ip = request.getRemoteAddress();
        return completedProvider.claim(claimData, ip, security.getUserPrincipal());
    }
}
