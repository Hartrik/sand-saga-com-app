package cz.harag.sandsaga.web.service;

import java.io.IOException;
import java.io.StringWriter;
import java.security.Principal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import cz.harag.sandsaga.web.security.DiscordPrincipal;
import cz.harag.sandsaga.web.security.FormPrincipal;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * @author Patrik Harag
 * @version 2024-03-02
 */
@ApplicationScoped
public class Templates {

    @ConfigProperty(name = "cz.harag.sandsaga.web.google-analytics.id")
    String googleAnalyticsId;

    @Inject
    Configuration configuration;

    public String build(String templatePath, SecurityContext securityContext, Map<String, ?> viewParameters) {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("google_analytics_id", googleAnalyticsId);

        // TODO: csrf protection
        map.put("_csrf", Map.of(
                "parameterName", "csrf-token",
                "token", UUID.randomUUID().toString()
        ));

        boolean logged = false;
        if (securityContext.isUserInRole("admin")) {
            map.put("user", "ADMIN");
            logged = true;
        } else if (securityContext.isUserInRole("user")) {
            map.put("user", "USER");
            logged = true;
        }
        if (logged) {
            Principal userPrincipal = securityContext.getUserPrincipal();
            if (userPrincipal != null) {
                map.put("user_name", userPrincipal.getName());
            }
            if (userPrincipal instanceof DiscordPrincipal) {
                map.put("user_tenant", "discord");
            } else if (userPrincipal instanceof FormPrincipal) {
                map.put("user_tenant", "form");
            }
        }

        map.putAll(viewParameters);

        try {
            Template template = configuration.getTemplate(templatePath);

            StringWriter stringWriter = new StringWriter();
            template.process(map, stringWriter);
            return stringWriter.toString();
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String build(String templatePath, SecurityContext securityContext) {
        return build(templatePath, securityContext, Collections.emptyMap());
    }
}
