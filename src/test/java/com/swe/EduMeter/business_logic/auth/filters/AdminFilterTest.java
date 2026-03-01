package com.swe.EduMeter.business_logic.auth.filters;

import com.swe.EduMeter.business_logic.auth.CryptoService;
import com.swe.EduMeter.model.Token;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.SecurityContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminFilterTest {

    @Mock
    private ContainerRequestContext requestContext;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AdminFilter adminFilter;

    // Helper method to safely bypass the parent `AuthFilter` logic.
    private void setupAuthFilterBypass(CryptoService mockCrypto, Token dummyToken) {
        when(requestContext.getHeaderString(anyString())).thenReturn("Bearer dummy-token");
        when(mockCrypto.decodeToken(anyString())).thenReturn(dummyToken);
    }

    @Test
    public void testFilter_AdminUser_Allowed() throws Exception {
        CryptoService mockCrypto = mock(CryptoService.class);
        // Create a dummy token representing an admin
        Token adminToken = new Token("admin_hash", Instant.now().plusSeconds(3600).toEpochMilli(), true);

        setupAuthFilterBypass(mockCrypto, adminToken);

        try (MockedStatic<CryptoService> mockedStatic = mockStatic(CryptoService.class)) {
            mockedStatic.when(CryptoService::getInstance).thenReturn(mockCrypto);

            // Mock the SecurityContext to return true for the ADMIN role
            when(requestContext.getSecurityContext()).thenReturn(securityContext);
            when(securityContext.isUserInRole("ADMIN")).thenReturn(true);

            assertDoesNotThrow(() -> adminFilter.filter(requestContext));

            verify(securityContext, times(1)).isUserInRole("ADMIN");
        }
    }

    @Test
    public void testFilter_StandardUser_Forbidden() throws Exception {
        CryptoService mockCrypto = mock(CryptoService.class);
        // Create a dummy token representing a standard user
        Token userToken = new Token("user_hash", Instant.now().plusSeconds(3600).toEpochMilli(), false);

        setupAuthFilterBypass(mockCrypto, userToken);

        try (MockedStatic<CryptoService> mockedStatic = mockStatic(CryptoService.class)) {
            mockedStatic.when(CryptoService::getInstance).thenReturn(mockCrypto);

            when(requestContext.getSecurityContext()).thenReturn(securityContext);
            when(securityContext.isUserInRole("ADMIN")).thenReturn(false);

            ForbiddenException exception = assertThrows(ForbiddenException.class,
                    () -> adminFilter.filter(requestContext));

            assertEquals("Forbidden", exception.getMessage());

            verify(securityContext, times(1)).isUserInRole("ADMIN");
        }
    }
}