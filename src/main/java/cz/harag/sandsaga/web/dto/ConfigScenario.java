package cz.harag.sandsaga.web.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author Patrik Harag
 * @version 2024-03-08
 */
@RegisterForReflection
public class ConfigScenario {

    private String name;
    private String title;
    private boolean scriptingEnabled;
    private boolean storeSnapshot;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
