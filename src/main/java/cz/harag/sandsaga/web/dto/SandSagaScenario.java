package cz.harag.sandsaga.web.dto;

/**
 * @author Patrik Harag
 * @version 2024-02-10
 */
public class SandSagaScenario {

    private String name;
    private Long entityId;
    private String title;

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
