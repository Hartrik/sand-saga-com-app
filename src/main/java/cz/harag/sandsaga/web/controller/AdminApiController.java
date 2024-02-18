package cz.harag.sandsaga.web.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cz.harag.sandsaga.web.dto.CompletedDto;
import cz.harag.sandsaga.web.dto.ReportDto;
import cz.harag.sandsaga.web.service.CompletedProvider;
import cz.harag.sandsaga.web.service.ReportProvider;
import cz.harag.sandsaga.web.service.SandSagaConfigProvider;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

/**
 * @author Patrik Harag
 * @version 2024-02-18
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

    @Inject
    CompletedProvider completedProvider;

    // server

    @POST
    @Path("/reload-config")
    @Produces(MediaType.APPLICATION_JSON)
    public void handleReloadConfig() {
        configProvider.reload();
    }

    @GET
    @Path("/server-status")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> handleGetStatus() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("limitCompletedEntity", completedProvider.getLimitEntityUsedRatio());
        result.put("limitCompletedAdditionalData", completedProvider.getLimitAdditionalDataUsedRatio());
        result.put("limitReportEntity", reportProvider.getLimitEntityUsedRatio());
        return result;
    }

    // completed

    @GET
    @Path("/completed")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CompletedDto> handleGetCompleted() {
        return completedProvider.list(0, 100);
    }

    @GET
    @Path("/completed/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CompletedDto handleGetCompleted(@PathParam("id") Long id) {
        return completedProvider.get(id);
    }

    @DELETE
    @Path("/completed/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void handleDeleteCompleted(@PathParam("id") Long id) {
        completedProvider.delete(id);
    }

    @GET
    @Path("/completed/{id}/snapshot.sgjs")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] handleGetCompletedSnapshot(@PathParam("id") Long id) {
        return completedProvider.getSnapshotData(id);
    }

    @DELETE
    @Path("/completed/{id}/snapshot.sgjs")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void handleDeleteCompletedSnapshot(@PathParam("id") Long id) {
        completedProvider.deleteSnapshotData(id);
    }

    // report

    @GET
    @Path("/reports")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReportDto> handleGetReports() {
        return reportProvider.list(0, 100);
    }

    @GET
    @Path("/reports/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ReportDto handleGetReport(@PathParam("id") Long id) {
        return reportProvider.get(id);
    }

    @DELETE
    @Path("/reports/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void handleDeleteReport(@PathParam("id") Long id) {
        reportProvider.delete(id);
    }

    @GET
    @Path("/reports/{id}/snapshot.sgjs")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] handleGetReportSnapshot(@PathParam("id") Long id) {
        return reportProvider.getSnapshotData(id);
    }
}
