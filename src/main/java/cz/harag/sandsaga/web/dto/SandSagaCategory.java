package cz.harag.sandsaga.web.dto;

import java.util.List;

/**
 * @author Patrik Harag
 * @version 2024-02-04
 */
public class SandSagaCategory {

    private String title;

    private List<SandSagaScenario> scenarios;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SandSagaScenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<SandSagaScenario> scenarios) {
        this.scenarios = scenarios;
    }
}
