package cz.harag.sandsaga.web.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author Patrik Harag
 * @version 2024-03-08
 */
@RegisterForReflection
public class SandGameConfig {

    private String versionSandGameJs;

    private String urlSandGameJsScript;
    private String urlSandGameJsCss;

    public String getUrlSandGameJsScript() {
        return urlSandGameJsScript;
    }

    public String getVersionSandGameJs() {
        return versionSandGameJs;
    }

    public void setVersionSandGameJs(String versionSandGameJs) {
        this.versionSandGameJs = versionSandGameJs;
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
}
