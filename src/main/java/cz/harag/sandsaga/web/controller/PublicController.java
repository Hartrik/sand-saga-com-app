package cz.harag.sandsaga.web.controller;

import cz.harag.sandsaga.web.dto.OutStatsScenarioDto;
import cz.harag.sandsaga.web.dto.SandSagaCategory;
import java.util.*;

import cz.harag.sandsaga.web.service.*;
import cz.harag.sandsaga.web.dto.SandSagaScenario;
import cz.harag.sandsaga.web.model.UserEntity;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import java.util.stream.Collectors;
import org.jboss.logging.Logger;

/**
 * @author Patrik Harag
 * @version 2024-03-22
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
    StatsProvider statsProvider;

    @Inject
    CompletedProvider completedProvider;

    @Inject
    TextsProvider textsProvider;

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public String indexHandler() {
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("config", config.getDefaultSandGameConfig());
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

    @GET
    @Path("/about")
    @Produces(MediaType.TEXT_HTML)
    public String aboutHandler() {
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("articleTitle", "About");
        parameters.put("articleContent", textsProvider.get(TextsProvider.KEY_ABOUT_PAGE));

        return templates.build("page-article.ftlh", security, parameters);
    }

    @GET
    @Path("/manual")
    @Produces(MediaType.TEXT_HTML)
    public String manualHandler() {
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("articleTitle", "Manual");
        parameters.put("articleContent", textsProvider.get(TextsProvider.KEY_MANUAL_PAGE));

        return templates.build("page-article.ftlh", security, parameters);
    }

    @GET
    @Path("/stats")
    @Produces(MediaType.TEXT_HTML)
    public String statsHandler() {
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("stats", liveStatsProvider.getStats());

        // sort and get title
        List<OutStatsScenarioDto> statsByScenario = statsProvider.listScenarioStats(0, 1000);
        Map<String, OutStatsScenarioDto> statsByScenarioMap = statsByScenario.stream()
                .collect(Collectors.toMap(OutStatsScenarioDto::getName, stats -> stats, (a, b) -> b, LinkedHashMap::new));

        List<OutStatsScenarioDto> statsByScenarioResult = new ArrayList<>(statsByScenario.size());

        for (SandSagaCategory category : config.categories()) {
            for (SandSagaScenario scenario : category.getScenarios()) {
                OutStatsScenarioDto stats = statsByScenarioMap.get(scenario.getName());
                if (stats != null) {
                    stats.setName(scenario.getTitle());
                    statsByScenarioResult.add(stats);
                }
            }
        }
        parameters.put("statsByScenario", statsByScenarioResult);

        return templates.build("page-stats.ftlh", security, parameters);
    }
}
