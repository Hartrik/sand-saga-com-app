package cz.harag.sandsaga.web.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author Patrik Harag
 * @version 2024-03-08
 */
@RegisterForReflection
public class SandSagaScenario {

    private String name;
    private Long entityId;
    private String title;

    private boolean scriptingEnabled;
    private boolean storeSnapshot;

    private String nextScenarioName;

    private String urlSandGameJsScript;
    private String urlSandGameJsCss;
    private String urlSandSagaScript;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getScriptingEnabled() {
        return scriptingEnabled;
    }

    public void setScriptingEnabled(boolean scriptingEnabled) {
        this.scriptingEnabled = scriptingEnabled;
    }

    public boolean getStoreSnapshot() {
        return storeSnapshot;
    }

    public void setStoreSnapshot(boolean storeSnapshot) {
        this.storeSnapshot = storeSnapshot;
    }

    public String getNextScenarioName() {
        return nextScenarioName;
    }

    public void setNextScenarioName(String nextScenarioName) {
        this.nextScenarioName = nextScenarioName;
    }

    public String getUrlSandGameJsScript() {
        return urlSandGameJsScript;
    }

    public void setUrlSandGameJsScript(String urlSandGameJsScript) {
        this.urlSandGameJsScript = urlSandGameJsScript;
    }

    public String getUrlSandGameJsCss() {
        return urlSandGameJsCss;
    }

    public void setUrlSandGameJsCss(String urlSandGameJsCss) {
        this.urlSandGameJsCss = urlSandGameJsCss;
    }

    public String getUrlSandSagaScript() {
        return urlSandSagaScript;
    }

    public void setUrlSandSagaScript(String urlSandSagaScript) {
        this.urlSandSagaScript = urlSandSagaScript;
    }
}
