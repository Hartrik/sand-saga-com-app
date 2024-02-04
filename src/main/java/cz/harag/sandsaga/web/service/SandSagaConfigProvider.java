package cz.harag.sandsaga.web.service;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.harag.sandsaga.web.dto.ConfigScenario;
import cz.harag.sandsaga.web.dto.SandSagaCategory;
import cz.harag.sandsaga.web.dto.ConfigRoot;
import cz.harag.sandsaga.web.dto.ConfigCategory;
import cz.harag.sandsaga.web.dto.SandSagaScenario;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

/**
 * @author Patrik Harag
 * @version 2024-02-04
 */
@ApplicationScoped
public class SandSagaConfigProvider {

    private static final Logger LOGGER = Logger.getLogger(SandSagaConfigProvider.class);

    @ConfigProperty(name = "cz.harag.sandsaga.config-file")
    String configFile;

    private final List<SandSagaCategory> cachedCategories = new ArrayList<>();
    private final Map<String, SandSagaScenario> cachedScenarios = new LinkedHashMap<>();

    void onStart(@Observes StartupEvent event) {
        try {
            ConfigRoot config = new ObjectMapper().readValue(new File(configFile), ConfigRoot.class);
            for (ConfigCategory configCategory : config.getCategories()) {
                SandSagaCategory sandSagaCategory = new SandSagaCategory();
                sandSagaCategory.setTitle(configCategory.getTitle());

                List<SandSagaScenario> sandSagaScenarios = new ArrayList<>(configCategory.getScenarios().size());
                for (ConfigScenario configScenario : configCategory.getScenarios()) {
                    SandSagaScenario sandSagaScenario = new SandSagaScenario();
                    sandSagaScenario.setName(configScenario.getName());
                    sandSagaScenario.setTitle(configScenario.getTitle());
                    sandSagaScenario.setUrlSandGameJsScript(config.getUrlSandGameJsScript());
                    sandSagaScenario.setUrlSandGameJsCss(config.getUrlSandGameJsCss());
                    String urlSandSagaScript = String.format(config.getUrlSandSagaScriptFormat(), configScenario.getName());
                    sandSagaScenario.setUrlSandSagaScript(urlSandSagaScript);

                    sandSagaScenarios.add(sandSagaScenario);
                    cachedScenarios.put(sandSagaScenario.getName(), sandSagaScenario);
                }
                sandSagaCategory.setScenarios(sandSagaScenarios);

                cachedCategories.add(sandSagaCategory);
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        LOGGER.info("Sand Saga config loaded, scenarios: " + cachedScenarios.size());
    }


    public SandSagaScenario scenario(String name) {
        return cachedScenarios.get(name);
    }

    public List<SandSagaCategory> categories() {
        return cachedCategories;
    }
}