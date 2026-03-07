package com.swe.EduMeter.business_logic.exception_handlers;

import com.swe.EduMeter.models.response.ApiError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExceptionHandlerTest {

    private final ExceptionHandler exceptionHandler = new ExceptionHandler();

    @Test
    public void TestToResponse_WebApplicationException() {
        String errorMessage = "Resource not found";
        int statusCode = Response.Status.NOT_FOUND.getStatusCode();

        WebApplicationException webEx = new WebApplicationException(errorMessage, statusCode);

        Response response = exceptionHandler.toResponse(webEx);

        assertNotNull(response, "Response should not be null");
        assertEquals(statusCode, response.getStatus(), "Status code should match the WebApplicationException status");
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType(), "Media type should be application/json");

        assertTrue(response.getEntity() instanceof ApiError, "Entity should be mapped to an ApiError object");

        ApiError apiError = (ApiError) response.getEntity();
        assertEquals(errorMessage, apiError.error(), "ApiError message should match the exception message");
    }

    @Test
    public void TestToResponse_StandardException() {
        String errorMessage = "Something went terribly wrong";
        Throwable standardException = new RuntimeException(errorMessage);

        Response response = exceptionHandler.toResponse(standardException);

        assertNotNull(response, "Response should not be null");
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus(), "Status code should default to 500 for generic exceptions");
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType(), "Media type should be application/json");

        assertTrue(response.getEntity() instanceof ApiError, "Entity should be mapped to an ApiError object");

        ApiError apiError = (ApiError) response.getEntity();
        assertEquals(errorMessage, apiError.error(), "ApiError message should match the generic exception message");
    }
}