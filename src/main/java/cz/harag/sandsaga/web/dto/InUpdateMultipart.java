package cz.harag.sandsaga.web.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

/**
 * @author Patrik Harag
 * @version 2024-02-18
 */
@RegisterForReflection
public class InUpdateMultipart {

    @FormParam("scenario")
    @PartType(MediaType.TEXT_PLAIN)
    public String scenario;
}