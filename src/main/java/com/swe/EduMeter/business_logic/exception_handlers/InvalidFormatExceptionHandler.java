package com.swe.EduMeter.business_logic.exception_handlers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.swe.EduMeter.model.response.ApiError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class InvalidFormatExceptionHandler implements ExceptionMapper<InvalidFormatException> {
    @Override
    public Response toResponse(InvalidFormatException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ApiError("Invalid field: '" + exception.getValue() + "'"))
                .build();
    }
}
