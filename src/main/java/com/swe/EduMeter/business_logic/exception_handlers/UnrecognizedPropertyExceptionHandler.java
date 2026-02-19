package com.swe.EduMeter.business_logic.exception_handlers;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.swe.EduMeter.model.response.ApiError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UnrecognizedPropertyExceptionHandler implements ExceptionMapper<UnrecognizedPropertyException> {
    @Override
    public Response toResponse(UnrecognizedPropertyException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ApiError("Unrecognized field '" + exception.getPropertyName() + "'"))
                .build();
    }
}
