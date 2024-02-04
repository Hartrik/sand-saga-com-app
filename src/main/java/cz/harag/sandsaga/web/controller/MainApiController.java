package cz.harag.sandsaga.web.controller;

import java.util.Map;

import cz.harag.sandsaga.web.dto.ReportMultipart;
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
 * @version 2024-02-04
 */
@Path("/api/user")
public class MainApiController {

    @Context
    SecurityContext security;

    @Inject
    ReportProvider reportProvider;

    @POST
    @Path("/report")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Object handlerReport(@MultipartForm ReportMultipart multipart, @Context HttpRequest request) {
        String ip = request.getRemoteAddress();

        Long id = reportProvider.report(multipart, ip);

        return Map.of("reportId", id);
    }
}
