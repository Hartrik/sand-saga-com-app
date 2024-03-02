package cz.harag.sandsaga.web.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;

import cz.harag.sandsaga.web.dto.SandSagaScenario;
import cz.harag.sandsaga.web.service.LiveStatsProvider;
import cz.harag.sandsaga.web.service.SandSagaConfigProvider;
import cz.harag.sandsaga.web.service.Templates;
import io.quarkus.oidc.OidcSession;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

/**
 * @author Patrik Harag
 * @version 2024-03-02
 */
@Path("/")
public class MainController {

    @Inject
    Templates templates;

    @Context
    SecurityContext security;

    @Inject
    SandSagaConfigProvider config;

    @Inject
    LiveStatsProvider liveStatsProvider;

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public String indexHandler() {
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("categories", config.categories());
        parameters.put("stats", liveStatsProvider.getStats());
        return templates.build("page-index.ftlh", security, parameters);
    }

    @GET
    @Path("/s/{scenario}/play")
    @Produces(MediaType.TEXT_HTML)
    public String indexHandler(@PathParam("scenario") String name) {
        SandSagaScenario scenario = config.scenario(name);
        if (scenario == null) {
            throw new NotFoundException();
        }

        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("config", config);
        parameters.put("scenario", scenario);
        return templates.build("page-scenario-play.ftlh", security, parameters);
    }

    @Inject
    OidcSession oidcSession;

    @GET
    @RolesAllowed("user")
    @Path("/logout")
    public Response logout() throws URISyntaxException {
        oidcSession.logout().await().indefinitely();
        return Response.seeOther(new URI("/")).build();
    }
}
