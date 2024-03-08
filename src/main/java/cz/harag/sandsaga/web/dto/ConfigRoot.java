package cz.harag.sandsaga.web.dto;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author Patrik Harag
 * @version 2024-03-08
 */
@RegisterForReflection
public class ConfigRoot {

    private String versionSandGameJs;

    private String urlSandGameJsScript;
    private String urlSandGameJsCss;
    private String urlSandSagaScriptFormat;  // with %s

    private List<ConfigCategory> categories;

    public String getVersionSandGameJs() {
        return versionSandGameJs;
    }

    public void setVersionSandGameJs(String versionSandGameJs) {
        this.versionSandGameJs = versionSandGameJs;
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

    public String getUrlSandSagaScriptFormat() {
        return urlSandSagaScriptFormat;
    }

    public void setUrlSandSagaScriptFormat(String urlSandSagaScriptFormat) {
        this.urlSandSagaScriptFormat = urlSandSagaScriptFormat;
    }

    public List<ConfigCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<ConfigCategory> categories) {
        this.categories = categories;
    }
}
