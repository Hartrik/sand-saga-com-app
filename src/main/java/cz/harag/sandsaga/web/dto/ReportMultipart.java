package cz.harag.sandsaga.web.dto;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

/**
 * @author Patrik Harag
 * @version 2024-02-04
 */
public class ReportMultipart {

    @FormParam("scenario")
    @PartType(MediaType.TEXT_PLAIN)
    public String scenario;

    @FormParam("location")
    @PartType(MediaType.TEXT_PLAIN)
    public String location;

    @FormParam("message")
    @PartType(MediaType.TEXT_PLAIN)
    public String message;

    @FormParam("metadata")
    @PartType(MediaType.TEXT_PLAIN)
    public String metadata;
}