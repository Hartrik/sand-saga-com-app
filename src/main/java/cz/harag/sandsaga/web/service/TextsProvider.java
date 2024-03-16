package cz.harag.sandsaga.web.service;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

/**
 * @author Patrik Harag
 * @version 2024-03-16
 */
@ApplicationScoped
public class TextsProvider {
    private static final Logger LOGGER = Logger.getLogger(TextsProvider.class);

    public static final String KEY_ABOUT_PAGE = "about-page";
    private static final String CONFIG_TEXT_FILE_PREFIX = "cz.harag.sandsaga.texts.file.";

    @ConfigProperty(name = "cz.harag.sandsaga.texts.enable-cache")
    boolean enableCache;

    private final Map<String, String> texts = new LinkedHashMap<>();

    void onStart(@Observes StartupEvent event) {
        if (enableCache) {
            LOGGER.info("Text cache enabled");
            reloadCache();
        } else {
            LOGGER.info("Text cache disabled");
        }
    }

    public String get(String key) {
        if (enableCache) {
            String value = texts.get(key);
            if (value == null) {
                LOGGER.error("Text not found: " + key);
                return "";
            }
            return value;
        } else {
            return load(key);
        }
    }

    public void reloadCache() {
        if (enableCache) {
            reloadCache(KEY_ABOUT_PAGE);
        }
    }

    private void reloadCache(String key) {
        String value = load(key);
        texts.put(key, value);
    }

    private String load(String key) {
        try {
            String path = ConfigProvider.getConfig().getValue(CONFIG_TEXT_FILE_PREFIX + key, String.class);
            String value = Files.readString(Paths.get(path));
            LOGGER.info("Text loaded: " + key);
            return value;
        } catch (Exception e) {
            LOGGER.error("Cannot load text: " + key, e);
            return null;
        }
    }
}