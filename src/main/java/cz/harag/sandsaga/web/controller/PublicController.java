package cz.harag.sandsaga.web.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import cz.harag.sandsaga.web.dto.SandSagaScenario;
import cz.harag.sandsaga.web.model.UserEntity;
import cz.harag.sandsaga.web.service.CompletedProvider;
import cz.harag.sandsaga.web.service.LiveStatsProvider;
import cz.harag.sandsaga.web.service.SandSagaConfigProvider;
import cz.harag.sandsaga.web.service.Templates;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import org.jboss.logging.Logger;

/**
 * @author Patrik Harag
 * @version 2024-03-03
 */
@Path("/")
public class PublicController {

    private static final Logger LOGGER = Logger.getLogger(PublicController.class);

    @Inject
    Templates templates;

    @Context
    SecurityContext security;

    @Inject
    SandSagaConfigProvider config;

    @Inject
    LiveStatsProvider liveStatsProvider;

    @Inject
    CompletedProvider completedProvider;

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public String indexHandler() {
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("categories", config.categories());
        parameters.put("stats", liveStatsProvider.getStats());

        try {
            Long userId = UserEntity.findByPrincipalAsId(security.getUserPrincipal());
            if (userId != null) {
                Map<String, Boolean> completedScenarios = completedProvider.completedScenarios(userId);
                if (!completedScenarios.isEmpty()) {
                    parameters.put("completed", completedScenarios);
                }
            }
        } catch (Exception e) {
            // front page should not fail
            LOGGER.error(e);
        }

        return templates.build("page-index.ftlh", security, parameters);
    }

    @GET
    @Path("/s/{scenario}/play")
    @Produces(MediaType.TEXT_HTML)
    public String scenarioHandler(@PathParam("scenario") String name) {
        SandSagaScenario scenario = config.scenario(name);
        if (scenario == null) {
            throw new NotFoundException();
        }

        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("config", config);
        parameters.put("scenario", scenario);
        return templates.build("page-scenario-play.ftlh", security, parameters);
    }
}
