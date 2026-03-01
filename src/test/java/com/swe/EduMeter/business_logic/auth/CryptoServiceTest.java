package com.swe.EduMeter.business_logic.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swe.EduMeter.model.Token;
import jakarta.ws.rs.NotAuthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.crypto.Mac;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

public class CryptoServiceTest {

    private CryptoService cryptoService;

    @BeforeEach
    public void setUp() {
        cryptoService = CryptoService.getInstance();
    }

    @Test
    public void testSingletonInstance() {
        CryptoService instance1 = CryptoService.getInstance();
        CryptoService instance2 = CryptoService.getInstance();

        assertSame(instance1, instance2, "getInstance() should return the exact same object");
    }

    @Test
    public void testGenerateAndDecodeToken_Valid() {
        String userHash = "user123_hash";
        boolean isAdmin = true;

        String encodedToken = cryptoService.generateToken(userHash, isAdmin);
        assertNotNull(encodedToken);
        assertTrue(encodedToken.contains("."));

        Token decodedToken = cryptoService.decodeToken(encodedToken);

        assertEquals(userHash, decodedToken.userHash());
        assertEquals(isAdmin, decodedToken.isAdmin());
        assertTrue(decodedToken.expiresAt() > Instant.now().toEpochMilli());
    }

    @Test
    public void testDecodeToken_Malformed() {
        String noDotToken = "just_a_random_string_without_dots";

        NotAuthorizedException exception1 = assertThrows(NotAuthorizedException.class,
                () -> cryptoService.decodeToken(noDotToken));
        assertEquals("Malformed token", exception1.getMessage());

        String tooManyDotsToken = "header.payload.signature.extra";
        NotAuthorizedException exception2 = assertThrows(NotAuthorizedException.class,
                () -> cryptoService.decodeToken(tooManyDotsToken));
        assertEquals("Malformed token", exception2.getMessage());
    }

    @Test
    public void testDecodeToken_InvalidSignature() {
        String encodedToken = cryptoService.generateToken("user1", false);

        // Tamper with the token by changing the last character
        String tamperedToken = encodedToken.substring(0, encodedToken.length() - 1) + "X";

        NotAuthorizedException exception = assertThrows(NotAuthorizedException.class,
                () -> cryptoService.decodeToken(tamperedToken));

        assertEquals("Invalid token signature", exception.getMessage());
    }

    @Test
    public void testRevokeToken() {
        String encodedToken = cryptoService.generateToken("user_to_revoke", false);

        // Prove it works initially
        assertDoesNotThrow(() -> cryptoService.decodeToken(encodedToken));

        // Revoke it
        cryptoService.revokeToken(encodedToken);

        // Prove it is now rejected
        NotAuthorizedException exception = assertThrows(NotAuthorizedException.class,
                () -> cryptoService.decodeToken(encodedToken));

        assertEquals("Revoked token", exception.getMessage());
    }

    @Test
    public void testDecodeToken_Expired() {
        Clock fixedClock = Clock.fixed(Instant.parse("2026-01-01T10:00:00Z"), ZoneId.systemDefault());
        Clock thirtyOneMinsLater = Clock.fixed(fixedClock.instant().plus(Duration.ofMinutes(31)), ZoneId.systemDefault());

        cryptoService.setClock(fixedClock);

        // Generate a token (this token will expire at 10:30:00Z)
        String token = cryptoService.generateToken("test_user", false);

        // Fast-forward to 31 minutes later (10:31:00Z)
        cryptoService.setClock(thirtyOneMinsLater);

        // 4. Try to decode it—it should now be considered expired
        NotAuthorizedException exception = assertThrows(NotAuthorizedException.class,
                () -> cryptoService.decodeToken(token));

        assertEquals("Expired token", exception.getMessage());
    }

    @Test
    public void testGetUserId() {
        String email1 = "Test.User@Example.COM";
        String email2 = "test.user@example.com";
        String email3 = "  test.user@example.com  ";

        // Should be deterministic and handle case-insensitivity + trimming
        String hash1 = cryptoService.getUserId(email1);
        String hash2 = cryptoService.getUserId(email2);
        String hash3 = cryptoService.getUserId(email3);

        assertEquals(hash1, hash2);
        assertEquals(hash2, hash3);

        // Verify length constraint (16 bytes base64 encoded without padding = 22 chars)
        assertEquals(22, hash1.length());

        // A completely different email should produce a different hash
        String differentHash = cryptoService.getUserId("another@example.com");
        assertNotEquals(hash1, differentHash);
    }
}