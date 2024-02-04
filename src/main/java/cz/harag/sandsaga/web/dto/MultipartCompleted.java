package cz.harag.sandsaga.web.dto;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

/**
 * @author Patrik Harag
 * @version 2024-02-04
 */
public class MultipartCompleted {

    // TODO

    @FormParam("metadata")
    @PartType(MediaType.TEXT_PLAIN)
    public String scenario;

    @FormParam("metadata")
    @PartType(MediaType.TEXT_PLAIN)
    public String metadata;

    @FormParam("image")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] image;
}