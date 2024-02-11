package cz.harag.sandsaga.web.dto;

/**
 * @author Patrik Harag
 * @version 2024-02-11
 */
public class SandGameConfig {

    private String urlSandGameJsScript;
    private String urlSandGameJsCss;

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
}
