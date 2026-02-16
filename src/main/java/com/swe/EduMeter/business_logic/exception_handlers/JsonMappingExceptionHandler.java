package com.swe.EduMeter.business_logic.exception_handlers;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class JsonMappingExceptionHandler implements ExceptionMapper<UnrecognizedPropertyException> {
    @Override
    public Response toResponse(UnrecognizedPropertyException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", "Unrecognized field '" + exception.getPropertyName() + "'"))
                .build();
    }
}
