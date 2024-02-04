package cz.harag.sandsaga.web.dto;

import java.util.List;

/**
 * @author Patrik Harag
 * @version 2024-02-04
 */
public class ConfigCategory {

    private String title;
    private String description;
    private List<ConfigScenario> scenarios;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ConfigScenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<ConfigScenario> scenarios) {
        this.scenarios = scenarios;
    }
}
