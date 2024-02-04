package cz.harag.sandsaga.web.controller;

import java.util.List;

import cz.harag.sandsaga.web.dto.ReportDto;
import cz.harag.sandsaga.web.service.ReportProvider;
import cz.harag.sandsaga.web.service.SandSagaConfigProvider;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
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

    @Inject
    ReportProvider reportProvider;

    @POST
    @Path("/reload-config")
    @Produces(MediaType.APPLICATION_JSON)
    public void handleReloadConfig() {
        configProvider.reload();
    }

    @GET
    @Path("/reports")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReportDto> handleGetReports() {
        return reportProvider.list();
    }
}
