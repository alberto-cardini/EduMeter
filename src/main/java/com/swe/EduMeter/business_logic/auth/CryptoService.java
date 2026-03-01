package com.swe.EduMeter.business_logic.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swe.EduMeter.model.Token;
import jakarta.ws.rs.NotAuthorizedException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/*
 * CryptoService is a singleton for security related management.
 * It requires the SECRET environment variable to be set,
 * which is then used to compute secure hashes. If the
 * variable is not set, it generates a random secret; this
 * invalidates key upon every deploy.
 *
 * It is based around the concept of JWTs
 * https://it.wikipedia.org/wiki/JSON_Web_Token
 *
 * In this implementation the header is omitted.
 */
public class CryptoService {
    private static final long TOKEN_EXPIRE_MINUTES = 30;
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static CryptoService instance = null;

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final Mac mac;
    private Clock clock = Clock.systemUTC();

    /* This shouldn't be used in production.
     * Instead, it is advised to use a redis key store,
     * which include key expiration.
     *
     * In that case, we would put every revoked token,
     * making it expire when it should have.
     *
     * That is to prevent this ConcurrentHashSet to become
     * too large without a scheduled cleaning.
     */
    private final Set<String> revokedTokens;

    public static synchronized CryptoService getInstance() {
        if (instance == null) {
            instance = new CryptoService();
        }

        return instance;
    }

    private CryptoService() {
        revokedTokens = ConcurrentHashMap.newKeySet();

        String secret = System.getenv("SECRET");
        byte[] secretBytes = new byte[32];

        if (secret == null) {
            System.err.println("SECRET environment variable not set, generating a random secret");
            new SecureRandom().nextBytes(secretBytes);
        } else {
            secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        }

        try {
            mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(secretBytes, HMAC_ALGORITHM);
            mac.init(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Bad algorithm or key");
        }
    }

    /**
     * Generate an encoded JWT Token.
     *
     * @param userHash  The user identifier.
     * @param isAdmin   Whether the user is an Admin or not.
     * @return          Base64 encoded token.
     */
    public String generateToken(String userHash, boolean isAdmin) {
        long expiresAt = Instant.now(clock).plus(Duration.ofMinutes(TOKEN_EXPIRE_MINUTES)).toEpochMilli();

        Token token = new Token(userHash, expiresAt, isAdmin);

        try {
            String jsonString = jsonMapper.writeValueAsString(token);

            // Generate payload signature
            byte[] signature = mac.doFinal(jsonString.getBytes(StandardCharsets.UTF_8));

            // Encode payload and signature
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(jsonString.getBytes(StandardCharsets.UTF_8));
            String encodedSignature = Base64.getUrlEncoder().withoutPadding().encodeToString(signature);

            return encodedPayload + "." + encodedSignature;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Couldn't serialize Token");
        }
    }

    /**
     * Decodes the encoded token, throwing errors if not valid.
     *
     * @param encodedToken Base64 encoded token
     * @return             The instance of the Token object, if valid.
     */
    public Token decodeToken(String encodedToken) {
        if (revokedTokens.contains(encodedToken)) {
            throw new NotAuthorizedException("Revoked token", "Bearer");
        }

        String[] parts = encodedToken.split("\\.");
        if (parts.length != 2) {
            throw new NotAuthorizedException("Malformed token", "Bearer");
        }

        byte[] payloadBytes = Base64.getUrlDecoder().decode(parts[0]);

        byte[] receivedSignature = Base64.getUrlDecoder().decode(parts[1]);
        byte[] computedSignature = mac.doFinal(payloadBytes);

        // This method is used to confront hashes as it is more robust
        // against attacks
        if (!MessageDigest.isEqual(receivedSignature, computedSignature)) {
            throw new NotAuthorizedException("Invalid token signature", "Bearer");
        }

        String jsonString = new String(payloadBytes, StandardCharsets.UTF_8);

        try {
            Token token = jsonMapper.readValue(jsonString, Token.class);

            if (Instant.now(clock).isAfter(Instant.ofEpochMilli(token.expiresAt()))) {
                throw new NotAuthorizedException("Expired token", "Bearer");
            }

            return token;
        } catch (JsonProcessingException e) {
            throw new NotAuthorizedException("Invalid token", "Bearer");
        }
    }

    /**
     * Revokes a valid token.
     * This method should be implemented with Redis
     * (@see TokenService.revokedTokens).
     *
     * @param encodedToken The token to revoke.
     */
    public void revokeToken(String encodedToken) {
        revokedTokens.add(encodedToken);
    }

    public String getUserId(String email) {
        byte[] fullHash = mac.doFinal(email.trim().toLowerCase().getBytes(StandardCharsets.UTF_8));
        // We use a 16bit version of the HMAC-SHA256 to have slimmer keys
        // for the DB, but still maintain a good collision resilience.
        byte[] truncatedHash = Arrays.copyOf(fullHash, 16);

        String encodedHash = Base64.getUrlEncoder().withoutPadding().encodeToString(truncatedHash);
        assert(encodedHash.length() == 22);

        return encodedHash;
    }

    // Used for testing
    void setClock(Clock clock) {
        this.clock = clock;
    }
}
