package cz.harag.sandsaga.web.service;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.harag.sandsaga.web.dto.ConfigScenario;
import cz.harag.sandsaga.web.dto.SandSagaCategory;
import cz.harag.sandsaga.web.dto.ConfigRoot;
import cz.harag.sandsaga.web.dto.ConfigCategory;
import cz.harag.sandsaga.web.dto.SandSagaScenario;
import cz.harag.sandsaga.web.model.ScenarioEntity;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

/**
 * @author Patrik Harag
 * @version 2024-02-10
 */
@ApplicationScoped
public class SandSagaConfigProvider {

    private static final Logger LOGGER = Logger.getLogger(SandSagaConfigProvider.class);

    @ConfigProperty(name = "cz.harag.sandsaga.config-file")
    String configFile;

    private List<SandSagaCategory> cachedCategories = new ArrayList<>();
    private Map<String, SandSagaScenario> cachedScenariosByName = new LinkedHashMap<>();
    private Map<Long, SandSagaScenario> cachedScenariosById = new LinkedHashMap<>();

    void onStart(@Observes StartupEvent event) {
        reload();
    }

    @Transactional
    public void reload() {
        List<SandSagaCategory> cachedCategories = new ArrayList<>();
        Map<String, SandSagaScenario> cachedScenariosByName = new LinkedHashMap<>();
        Map<Long, SandSagaScenario> cachedScenariosById = new LinkedHashMap<>();
        try {
            ConfigRoot configRoot = new ObjectMapper().readValue(new File(configFile), ConfigRoot.class);
            for (ConfigCategory configCategory : configRoot.getCategories()) {
                SandSagaCategory sandSagaCategory = new SandSagaCategory();
                sandSagaCategory.setTitle(configCategory.getTitle());

                List<SandSagaScenario> sandSagaScenarios = new ArrayList<>(configCategory.getScenarios().size());
                for (ConfigScenario configScenario : configCategory.getScenarios()) {
                    SandSagaScenario sandSagaScenario = getSandSagaScenario(configRoot, configScenario);

                    sandSagaScenarios.add(sandSagaScenario);
                    cachedScenariosByName.put(sandSagaScenario.getName(), sandSagaScenario);
                    cachedScenariosById.put(sandSagaScenario.getEntityId(), sandSagaScenario);
                }
                sandSagaCategory.setScenarios(sandSagaScenarios);

                cachedCategories.add(sandSagaCategory);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        this.cachedCategories = cachedCategories;
        this.cachedScenariosByName = cachedScenariosByName;
        this.cachedScenariosById = cachedScenariosById;

        LOGGER.info("Sand Saga config loaded, scenarios: " + cachedScenariosById.size());
    }

    private static SandSagaScenario getSandSagaScenario(ConfigRoot configRoot, ConfigScenario configScenario) {
        SandSagaScenario sandSagaScenario = new SandSagaScenario();
        sandSagaScenario.setName(configScenario.getName());
        sandSagaScenario.setTitle(configScenario.getTitle());

        // resolve urls
        sandSagaScenario.setUrlSandGameJsScript(configRoot.getUrlSandGameJsScript());
        sandSagaScenario.setUrlSandGameJsCss(configRoot.getUrlSandGameJsCss());
        String urlSandSagaScript = String.format(configRoot.getUrlSandSagaScriptFormat(), configScenario.getName());
        sandSagaScenario.setUrlSandSagaScript(urlSandSagaScript);

        // resolve entity id
        Optional<ScenarioEntity> optional = ScenarioEntity.findByName(configScenario.getName()).firstResultOptional();
        if (optional.isPresent()) {
            sandSagaScenario.setEntityId(optional.get().id);
        } else {
            ScenarioEntity entity = new ScenarioEntity();
            entity.name = configScenario.getName();
            ScenarioEntity.persist(entity);
            sandSagaScenario.setEntityId(entity.id);
        }

        return sandSagaScenario;
    }

    public SandSagaScenario scenario(String name) {
        return cachedScenariosByName.get(name);
    }

    public SandSagaScenario scenario(Long id) {
        return cachedScenariosById.get(id);
    }

    public List<SandSagaCategory> categories() {
        return cachedCategories;
    }
}