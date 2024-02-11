package cz.harag.sandsaga.web.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

/**
 * @author Patrik Harag
 * @version 2024-02-10
 */
@RegisterForReflection
public class MultipartReport {

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

    @FormParam("data")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] data;
}