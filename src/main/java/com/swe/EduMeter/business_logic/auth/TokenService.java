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
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

/*
 * TokenService is a singleton for Token management.
 * It requires the SECRET environment variable to be set,
 * which is then used to compute Token signatures.
 *
 * It is based around the concept of JWTs
 * https://it.wikipedia.org/wiki/JSON_Web_Token
 *
 * In this implementation the header is omitted.
 */
public class TokenService {
    private static final long TOKEN_EXPIRE_MINUTES = 30;
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static TokenService instance = null;

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final Mac mac;

    public static synchronized TokenService getInstance() {
        if (instance == null) {
            instance = new TokenService();
        }

        return instance;
    }

    public TokenService() {
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
    public String generateToken(String userHash, boolean isAdmin) {
        long expiresAt = Instant.now().plus(Duration.ofMinutes(TOKEN_EXPIRE_MINUTES)).toEpochMilli();
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

    public Token decodeToken(String encodedToken) {
        String[] parts = encodedToken.split("\\.");
        if (parts.length != 2) {
            throw new NotAuthorizedException("Malformed token");
        }

        byte[] payloadBytes = Base64.getUrlDecoder().decode(parts[0]);

        byte[] receivedSignature = Base64.getUrlDecoder().decode(parts[1]);
        byte[] computedSignature = mac.doFinal(payloadBytes);

        // This method is used to confront hashes as it is more robust
        // against attacks
        if (!MessageDigest.isEqual(receivedSignature, computedSignature)) {
            throw new NotAuthorizedException("Invalid token signature");
        }

        String jsonString = new String(payloadBytes, StandardCharsets.UTF_8);

        try {
            Token token = jsonMapper.readValue(jsonString, Token.class);

            if (Instant.now().isAfter(Instant.ofEpochMilli(token.expiresAt()))) {
                throw new NotAuthorizedException("Expired token");
            }

            return token;
        } catch (JsonProcessingException e) {
            throw new NotAuthorizedException("Invalid token");
        }
    }
}
