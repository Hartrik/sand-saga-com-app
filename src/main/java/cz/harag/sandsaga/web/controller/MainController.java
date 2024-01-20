package cz.harag.sandsaga.web.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import cz.harag.sandsaga.web.ConfigScenarios;
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
 * @version 2024-01-20
 */
@Path("/")
public class MainController {

    @Inject
    Templates templates;

    @Context
    SecurityContext security;

    @Inject
    ConfigScenarios scenarios;

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public String indexHandler() {
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("scenarios", scenarios);
        return templates.build("page-index.ftlh", security, parameters);
    }

    @GET
    @Path("/s/{scenario}/play")
    @Produces(MediaType.TEXT_HTML)
    public String indexHandler(@PathParam("scenario") String name) {
        ConfigScenarios.ConfigScenario scenario = getScenario(name);
        if (scenario == null) {
            throw new NotFoundException();
        }

        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("scenarios", scenarios);
        parameters.put("scenario", scenario);
        return templates.build("page-scenario-play.ftlh", security, parameters);
    }

    private ConfigScenarios.ConfigScenario getScenario(String name) {
        for (ConfigScenarios.ConfigScenario scenario : scenarios.tutorial()) {
            if (scenario.name().equals(name)) {
                return scenario;
            }
        }
        return null;
    }
}
