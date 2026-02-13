package com.swe.EduMeter.business_logic;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

/*
 * Handles every type of exception that is thrown within
 * the application context.
 *
 * Standard response for an exception is an HTML page,
 * which is not very well suited for a backend API.
 * Thus, every exception is mapped to a corresponding JSON
 * error:
 *
 * ```json
 * {
 *     "error": "message"
 * }
 * ```
 *
 * WebApplicationExceptions are mapped to JSON objects
 * of the same type, but their status code is preserved
 * (e.g. NotFoundException will also return a 404). Other
 * Exceptions are all mapped to 500 - Internal Server Error.
 *
 * This handler doesn't need to be registered anywhere, the
 * @Provider annotation makes it discoverable by jax-rs.
 */
@Provider
public class ExceptionHandler implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof WebApplicationException webEx) {
            return Response
                    .status(webEx.getResponse().getStatus())
                    .entity(Map.of("error", webEx.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("error", exception.getMessage()))
                .build();
    }
}
