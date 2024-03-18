package cz.harag.sandsaga.web.controller;

import cz.harag.sandsaga.web.service.*;
import jakarta.ws.rs.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cz.harag.sandsaga.web.dto.OutCompletedDto;
import cz.harag.sandsaga.web.dto.OutStatsDayDto;
import cz.harag.sandsaga.web.dto.OutReportDto;
import cz.harag.sandsaga.web.dto.OutStatsDto;
import cz.harag.sandsaga.web.dto.OutStatsScenarioDto;
import cz.harag.sandsaga.web.dto.OutUserDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;

/**
 * @author Patrik Harag
 * @version 2024-03-15
 */
@RolesAllowed("admin")
@Path("/api/admin")
public class AdminApiController {

    @Inject
    SandSagaConfigProvider configProvider;

    @Inject
    ReportProvider reportProvider;

    @Inject
    CompletedProvider completedProvider;

    @Inject
    StatsProvider statsProvider;

    @Inject
    LiveStatsProvider liveStatsProvider;

    @Inject
    UserProvider userProvider;

    @Inject
    TextsProvider textsProvider;

    // server

    @POST
    @Path("/reload-config")
    @Produces(MediaType.APPLICATION_JSON)
    public void handleReloadConfig() {
        configProvider.reload();
    }

    @POST
    @Path("/reload-texts")
    @Produces(MediaType.APPLICATION_JSON)
    public void handleReloadTexts() {
        textsProvider.reloadCache();
    }

    @POST
    @Path("/update-stats-from-completed")
    @Produces(MediaType.APPLICATION_JSON)
    public void handleUpdateStatsFromCompleted() {
        statsProvider.updateStatsFromCompleted();
    }

    @POST
    @Path("/refresh-live-stats")
    @Produces(MediaType.APPLICATION_JSON)
    public void handleRefreshLiveStats() {
        liveStatsProvider.refresh();
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
    public List<OutCompletedDto> handleGetCompleted(@QueryParam("withSnapshotOnly") Boolean withSnapshotOnly) {
        return completedProvider.list(0, 100, withSnapshotOnly);
    }

    @GET
    @Path("/completed/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public OutCompletedDto handleGetCompleted(@PathParam("id") Long id) {
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
        completedProvider.deleteSnapshotData(id, true);
    }

    // report

    @GET
    @Path("/reports")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OutReportDto> handleGetReports() {
        return reportProvider.list(0, 100);
    }

    @GET
    @Path("/reports/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public OutReportDto handleGetReport(@PathParam("id") Long id) {
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

    // stats

    @GET
    @Path("/stats/by-scenario")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OutStatsScenarioDto> handleGetStatsByScenario() {
        return statsProvider.listScenarioStats(0, 30);
    }

    @GET
    @Path("/stats/by-scenario/sum")
    @Produces(MediaType.APPLICATION_JSON)
    public OutStatsDto handleGetStatsByScenarioSum() {
        return statsProvider.sumScenarioStats();
    }

    @GET
    @Path("/stats/by-day")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OutStatsDayDto> handleGetStatsByDay() {
        return statsProvider.listDayStats(0, 30);
    }

    @GET
    @Path("/stats/by-day/sum")
    @Produces(MediaType.APPLICATION_JSON)
    public OutStatsDto handleGetStatsByDaySum() {
        return statsProvider.sumDayStats();
    }

    // users

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OutUserDto> handleGetUsers() {
        return userProvider.list(0, 100);
    }

    @GET
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public OutUserDto handleGetUser(@PathParam("id") Long id) {
        return userProvider.get(id);
    }

    @DELETE
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void handleDeleteUser(@PathParam("id") Long id) {
        userProvider.delete(id);
    }
}
