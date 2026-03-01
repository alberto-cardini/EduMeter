package com.swe.EduMeter.business_logic.auth.filters;

import com.swe.EduMeter.business_logic.auth.CryptoService;
import com.swe.EduMeter.model.Token;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.SecurityContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.security.Principal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthFilterTest {

    @Mock
    private ContainerRequestContext requestContext;

    @Mock
    private SecurityContext originalSecurityContext;

    @Captor
    private ArgumentCaptor<SecurityContext> securityContextCaptor;

    @InjectMocks
    private AuthFilter authFilter;

    @Test
    public void testFilter_MissingHeader() {
        when(requestContext.getHeaderString("Authorization")).thenReturn(null);

        NotAuthorizedException exception = assertThrows(NotAuthorizedException.class,
                () -> authFilter.filter(requestContext));

        assertEquals("Invalid authentication header", exception.getMessage());
        verify(requestContext, never()).setSecurityContext(any(SecurityContext.class));
    }

    @Test
    public void testFilter_MalformedHeader() {
        when(requestContext.getHeaderString("Authorization")).thenReturn("Basic dXNlcm5hbWU6cGFzc3dvcmQ=");

        NotAuthorizedException exception = assertThrows(NotAuthorizedException.class,
                () -> authFilter.filter(requestContext));

        assertEquals("Invalid authentication header", exception.getMessage());
        verify(requestContext, never()).setSecurityContext(any(SecurityContext.class));
    }

    @Test
    public void testFilter_ValidAdminToken() throws IOException {
        String tokenString = "valid_encoded_admin_token";
        when(requestContext.getHeaderString("Authorization")).thenReturn("Bearer " + tokenString);
        when(requestContext.getSecurityContext()).thenReturn(originalSecurityContext);
        when(originalSecurityContext.isSecure()).thenReturn(true);

        CryptoService mockCrypto = mock(CryptoService.class);
        Token adminToken = new Token("admin_hash_123", Instant.now().plusSeconds(3600).toEpochMilli(), true);
        when(mockCrypto.decodeToken(tokenString)).thenReturn(adminToken);

        try (MockedStatic<CryptoService> mockedStatic = mockStatic(CryptoService.class)) {
            mockedStatic.when(CryptoService::getInstance).thenReturn(mockCrypto);

            assertDoesNotThrow(() -> authFilter.filter(requestContext));

            // Capture the custom SecurityContext that was injected
            verify(requestContext).setSecurityContext(securityContextCaptor.capture());
            SecurityContext injectedContext = securityContextCaptor.getValue();

            // Verify the injected SecurityContext behaves correctly for an Admin
            Principal principal = injectedContext.getUserPrincipal();
            assertNotNull(principal);
            assertEquals("admin_hash_123", principal.getName());

            assertTrue(injectedContext.isUserInRole("ADMIN"));
            assertFalse(injectedContext.isUserInRole("BASE"));
            assertFalse(injectedContext.isUserInRole("UNKNOWN_ROLE"));

            assertEquals("Bearer", injectedContext.getAuthenticationScheme());
            assertTrue(injectedContext.isSecure()); // Delegates to the original context
        }
    }

    @Test
    public void testFilter_ValidBaseUserToken() throws IOException {
        String tokenString = "valid_encoded_base_token";
        when(requestContext.getHeaderString("Authorization")).thenReturn("Bearer " + tokenString);
        when(requestContext.getSecurityContext()).thenReturn(originalSecurityContext);
        when(originalSecurityContext.isSecure()).thenReturn(false);

        CryptoService mockCrypto = mock(CryptoService.class);
        Token baseToken = new Token("user_hash_456", Instant.now().plusSeconds(3600).toEpochMilli(), false);
        when(mockCrypto.decodeToken(tokenString)).thenReturn(baseToken);

        try (MockedStatic<CryptoService> mockedStatic = mockStatic(CryptoService.class)) {
            mockedStatic.when(CryptoService::getInstance).thenReturn(mockCrypto);

            assertDoesNotThrow(() -> authFilter.filter(requestContext));

            // Capture the custom SecurityContext that was injected
            verify(requestContext).setSecurityContext(securityContextCaptor.capture());
            SecurityContext injectedContext = securityContextCaptor.getValue();

            // Verify the injected SecurityContext behaves correctly for a Base user
            Principal principal = injectedContext.getUserPrincipal();
            assertNotNull(principal);
            assertEquals("user_hash_456", principal.getName());

            assertFalse(injectedContext.isUserInRole("ADMIN"));
            assertTrue(injectedContext.isUserInRole("BASE"));

            assertEquals("Bearer", injectedContext.getAuthenticationScheme());
            assertFalse(injectedContext.isSecure()); // Delegates to the original context
        }
    }

    @Test
    public void testFilter_InvalidTokenFromCryptoService() {
        String tokenString = "expired_or_tampered_token";
        when(requestContext.getHeaderString("Authorization")).thenReturn("Bearer " + tokenString);

        CryptoService mockCrypto = mock(CryptoService.class);
        // Simulate CryptoService throwing an exception (e.g., token expired)
        when(mockCrypto.decodeToken(tokenString)).thenThrow(new NotAuthorizedException("Expired token", "Bearer"));

        try (MockedStatic<CryptoService> mockedStatic = mockStatic(CryptoService.class)) {
            mockedStatic.when(CryptoService::getInstance).thenReturn(mockCrypto);

            NotAuthorizedException exception = assertThrows(NotAuthorizedException.class,
                    () -> authFilter.filter(requestContext));

            assertEquals("Expired token", exception.getMessage());

            // Ensure we never reached the point of overriding the SecurityContext
            verify(requestContext, never()).setSecurityContext(any(SecurityContext.class));
        }
    }
}