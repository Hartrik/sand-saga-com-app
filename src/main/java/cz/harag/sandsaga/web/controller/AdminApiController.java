package cz.harag.sandsaga.web.controller;

import cz.harag.sandsaga.web.service.SandSagaConfigProvider;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

/**
 * @author Patrik Harag
 * @version 2024-02-04
 */
@RolesAllowed("admin")
@Path("/api/admin")
public class AdminApiController {

    @Context
    SecurityContext security;

    @Inject
    SandSagaConfigProvider configProvider;

    @POST
    @Path("/reload-config")
    @Produces(MediaType.APPLICATION_JSON)
    public void handleReloadConfig() {
        configProvider.reload();
    }
}
