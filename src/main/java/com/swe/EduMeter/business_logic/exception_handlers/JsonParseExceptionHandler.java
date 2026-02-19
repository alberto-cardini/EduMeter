package com.swe.EduMeter.business_logic.exception_handlers;

import com.fasterxml.jackson.core.JsonParseException;
import com.swe.EduMeter.model.response.ApiError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class JsonParseExceptionHandler implements ExceptionMapper<JsonParseException> {
    @Override
    public Response toResponse(JsonParseException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ApiError("Invalid JSON body"))
                .build();
    }
}
