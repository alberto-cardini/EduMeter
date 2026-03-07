package com.swe.EduMeter.business_logic.exception_handlers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.swe.EduMeter.models.response.ApiError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvalidFormatExceptionHandlerTest {

    private final InvalidFormatExceptionHandler handler = new InvalidFormatExceptionHandler();

    @Mock
    private InvalidFormatException mockException;

    @Test
    public void TestToResponse_InvalidFormat() {
        String invalidInputValue = "not_a_valid_number";

        when(mockException.getValue()).thenReturn(invalidInputValue);

        Response response = handler.toResponse(mockException);

        assertNotNull(response, "Response should not be null");
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus(), "Status code should be 400 Bad Request");
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType(), "Media type should be application/json");

        assertTrue(response.getEntity() instanceof ApiError, "Entity should be mapped to an ApiError object");

        ApiError apiError = (ApiError) response.getEntity();

        String expectedMessage = "Invalid field: '" + invalidInputValue + "'";
        assertEquals(expectedMessage, apiError.error(), "ApiError message should contain the invalid value");
    }
}