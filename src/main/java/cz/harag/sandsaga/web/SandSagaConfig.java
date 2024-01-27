package cz.harag.sandsaga.web;

import java.util.List;

import io.smallrye.config.ConfigMapping;

/**
 * @author Patrik Harag
 * @version 2024-01-27
 */
@ConfigMapping(prefix = "sandsaga")
public interface SandSagaConfig {

    String urlSandGameJsScript();
    String urlSandGameCss();
    String urlSandSagaScript();  // with %s

    List<ConfigCategory> categories();

    interface ConfigCategory {
        String title();
//        String description();
        List<ConfigScenario> scenarios();
    }

    interface ConfigScenario {
        String name();
        String title();
    }
}
