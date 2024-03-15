package cz.harag.sandsaga.web.service;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;

/**
 * @author Patrik Harag
 * @version 2024-03-15
 */
@ApplicationScoped
public class TextsProvider {
    private static final Logger LOGGER = Logger.getLogger(TextsProvider.class);

    public static final String KEY_ABOUT_PAGE = "about-page";

    private final Map<String, String> texts = new LinkedHashMap<>();

    void onStart(@Observes StartupEvent event) {
        reload();
    }

    public String get(String key) {
        String value = texts.get(key);
        if (value == null) {
            LOGGER.error("Text not found: " + key);
            return "";
        }
        return value;
    }

    public void reload() {
        load(KEY_ABOUT_PAGE);
    }

    private void load(String key) {
        try {
            String path = ConfigProvider.getConfig().getValue("cz.harag.sandsaga.text." + key, String.class);
            String value = Files.readString(Paths.get(path));
            texts.put(key, value);
        } catch (Exception e) {
            LOGGER.error("Cannot load text: " + key, e);
            return;
        }
        LOGGER.info("Text loaded: " + key);
    }
}