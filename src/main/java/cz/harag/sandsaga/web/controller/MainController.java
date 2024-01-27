package cz.harag.sandsaga.web.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import cz.harag.sandsaga.web.SandSagaConfig;
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

/**
 * @author Patrik Harag
 * @version 2024-01-27
 */
@Path("/")
public class MainController {

    @Inject
    Templates templates;

    @Context
    SecurityContext security;

    @Inject
    SandSagaConfig config;

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public String indexHandler() {
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("config", config);
        return templates.build("page-index.ftlh", security, parameters);
    }

    @GET
    @Path("/s/{scenario}/play")
    @Produces(MediaType.TEXT_HTML)
    public String indexHandler(@PathParam("scenario") String name) {
        SandSagaConfig.ConfigScenario scenario = getScenario(name);
        if (scenario == null) {
            throw new NotFoundException();
        }

        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("config", config);
        parameters.put("scenario", scenario);
        return templates.build("page-scenario-play.ftlh", security, parameters);
    }

    private SandSagaConfig.ConfigScenario getScenario(String name) {
        // TODO: optimize
        for (SandSagaConfig.ConfigCategory category : config.categories()) {
            for (SandSagaConfig.ConfigScenario scenario : category.scenarios()) {
                if (scenario.name().equals(name)) {
                    return scenario;
                }
            }
        }
        return null;
    }
}
