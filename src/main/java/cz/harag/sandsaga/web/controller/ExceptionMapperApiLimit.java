package cz.harag.sandsaga.web.controller;

import cz.harag.sandsaga.web.service.ApiLimitExceeded;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * @author Patrik Harag
 * @version 2024-02-10
 */
@Provider
public class ExceptionMapperApiLimit implements ExceptionMapper<ApiLimitExceeded> {

    @Override
    @Produces(MediaType.TEXT_PLAIN)
    public Response toResponse(ApiLimitExceeded exception) {
        return Response.status(503).entity("API limit exceeded").build();
    }
}