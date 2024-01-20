package cz.harag.sandsaga.web;

import java.util.List;

import io.smallrye.config.ConfigMapping;

/**
 * @author Patrik Harag
 * @version 2024-01-20
 */
@ConfigMapping(prefix = "scenarios")
public interface ConfigScenarios {

    String urlSandGameJsScript();
    String urlSandGameCss();
    String urlSandSagaScript();  // with %s

    List<ConfigScenario> tutorial();

    interface ConfigScenario {
        String name();
        String title();
    }
}