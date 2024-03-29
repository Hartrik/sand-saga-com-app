package cz.harag.sandsaga.web.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import cz.harag.sandsaga.web.service.SandSagaConfigProvider;
import cz.harag.sandsaga.web.service.Templates;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

/**
 * @author Patrik Harag
 * @version 2024-02-11
 */
@RolesAllowed("admin")
@Path("/admin")
public class AdminController {

    @Inject
    Templates templates;

    @Context
    SecurityContext security;

    @Inject
    SandSagaConfigProvider config;

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public String indexHandler() {
        Map<String, Object> parameters = new LinkedHashMap<>();
        return templates.build("page-admin.ftlh", security, parameters);
    }

    @GET
    @Path("/completed/{id}/snapshot.sgjs")
    @Produces(MediaType.TEXT_HTML)
    public String handlePlayCompletedSnapshot(@PathParam("id") Long id) {
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("config", config.getDefaultSandGameConfig());
        parameters.put("snapshotUrl", "/api/admin/completed/" + id + "/snapshot.sgjs");
        return templates.build("page-admin-snapshot-play.ftlh", security, parameters);
    }

    @GET
    @Path("/reports/{id}/snapshot.sgjs")
    @Produces(MediaType.TEXT_HTML)
    public String handlePlayReportSnapshot(@PathParam("id") Long id) {
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("config", config.getDefaultSandGameConfig());
        parameters.put("snapshotUrl", "/api/admin/reports/" + id + "/snapshot.sgjs");
        return templates.build("page-admin-snapshot-play.ftlh", security, parameters);
    }
}
