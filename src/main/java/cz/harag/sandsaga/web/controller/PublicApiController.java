package cz.harag.sandsaga.web.controller;

import java.util.Map;

import cz.harag.sandsaga.web.dto.InCompletedMultipart;
import cz.harag.sandsaga.web.dto.InReportMultipart;
import cz.harag.sandsaga.web.dto.InUpdateMultipart;
import cz.harag.sandsaga.web.service.UpdateProvider;
import cz.harag.sandsaga.web.service.CompletedProvider;
import cz.harag.sandsaga.web.service.ReportProvider;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.spi.HttpRequest;

/**
 * @author Patrik Harag
 * @version 2024-03-02
 */
@Path("/api/user")
public class PublicApiController {

    @Context
    SecurityContext security;

    @Inject
    ReportProvider reportProvider;

    @Inject
    CompletedProvider completedProvider;

    @Inject
    UpdateProvider updateProvider;

    @POST
    @Path("/report")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Object handlerReport(@MultipartForm InReportMultipart multipart, @Context HttpRequest request) {
        String ip = request.getRemoteAddress();
        Long id = reportProvider.report(multipart, ip, security.getUserPrincipal());

        return Map.of("id", id);
    }

    @POST
    @Path("/scenario-completed")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Object handleCompleted(@MultipartForm InCompletedMultipart multipart, @Context HttpRequest request) {
        String ip = request.getRemoteAddress();
        Long id = completedProvider.store(multipart, ip, security.getUserPrincipal());

        return Map.of("id", id);
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public void handleUpdate(@MultipartForm InUpdateMultipart multipart, @Context HttpRequest request) {
        String ip = request.getRemoteAddress();
        updateProvider.update(multipart, ip);
    }
}
