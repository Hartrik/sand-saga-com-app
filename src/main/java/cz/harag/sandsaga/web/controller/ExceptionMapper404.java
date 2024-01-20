package cz.harag.sandsaga.web.controller;

import cz.harag.sandsaga.web.service.Templates;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * @author Patrik Harag
 * @version 2024-01-20
 */
@Provider
public class ExceptionMapper404 implements ExceptionMapper<NotFoundException> {

    @Inject
    Templates templates;

    @Context
    SecurityContext security;

    @Override
    @Produces(MediaType.TEXT_HTML)
    public Response toResponse(NotFoundException exception) {
        String html = templates.build("page-404.ftlh", security);
        return Response.status(404).entity(html).build();
    }
}