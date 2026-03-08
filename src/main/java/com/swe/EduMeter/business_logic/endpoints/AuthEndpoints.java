package com.swe.EduMeter.business_logic.endpoints;

import com.swe.EduMeter.business_logic.auth.CryptoService;
import com.swe.EduMeter.business_logic.auth.annotations.AuthGuard;
import com.swe.EduMeter.models.PinChallenge;
import com.swe.EduMeter.models.User;
import com.swe.EduMeter.models.response.ApiOk;
import com.swe.EduMeter.orm.dao.AdminDAO;
import com.swe.EduMeter.orm.dao.PinChallengeDAO;
import com.swe.EduMeter.orm.dao.UserDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;


@Path("/auth")
public class AuthEndpoints {
    public static final String[] WHITELISTED_DOMAINS = { "edu.unifi.it" };
    public static final int PIN_EXPIRATION_MIN = 10;
    private final PinChallengeDAO pinDAO;
    private final AdminDAO adminDAO;
    private final UserDAO userDAO;

    @Inject
    public AuthEndpoints(PinChallengeDAO pinChallengeDAO, AdminDAO adminDAO, UserDAO userDAO) {
        this.pinDAO = pinChallengeDAO;
        this.adminDAO = adminDAO;
        this.userDAO = userDAO;
    }

    @POST
    @Path("/sendPin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SendPinResponse sendPin(@QueryParam("admin") boolean isAdmin,
                         SendPinBody body) {
        if (body.email() == null) {
            throw new BadRequestException("Missing field 'email'");
        }

        if (isAdmin) {
            adminDAO.getByEmail(body.email().toLowerCase())
                    .orElseThrow(() -> new ForbiddenException("Invalid admin email"));

            String veryRandomPin = "1234";
            String userHash = CryptoService.getInstance().getUserId(body.email());
            Instant expiresAt = Instant.now().plus(Duration.ofMinutes(PIN_EXPIRATION_MIN));
            PinChallenge pinChallenge = new PinChallenge(null, veryRandomPin, userHash, expiresAt, true);

            int challengeId = pinDAO.add(pinChallenge);
            // SHOULD send pin via email

            return new SendPinResponse(challengeId);
        }

        for (String domain: WHITELISTED_DOMAINS) {
            if (body.email().endsWith("@" + domain)) {
                String veryRandomPin = "1234";
                String userHash = CryptoService.getInstance().getUserId(body.email());
                Instant expiresAt = Instant.now().plus(Duration.ofMinutes(PIN_EXPIRATION_MIN));
                PinChallenge pinChallenge = new PinChallenge(null, veryRandomPin, userHash, expiresAt, false);

                int challengeId = pinDAO.add(pinChallenge);
                // SHOULD send pin via email

                return new SendPinResponse(challengeId);
            }
        }

        throw new ForbiddenException("Invalid email domain");
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public LoginResponse login(@QueryParam("admin") boolean isAdmin,
                               LoginBody body) {
        if (body.pin() == null) {
            throw new BadRequestException("Missing field 'pin'");
        }
        if (body.challengeId() == null) {
            throw new BadRequestException("Missing field 'challengeId'");
        }

        PinChallenge pinChallenge = pinDAO.get(body.challengeId())
                                .orElseThrow(() -> new NotFoundException("Invalid challengeId"));

        if (Instant.now().isAfter(pinChallenge.getExpiresAt())) {
            throw new NotAuthorizedException("Pin challenge expired", "Bearer");
        }

        if (!pinChallenge.getPin().equals(body.pin())) {
            throw new NotAuthorizedException("Invalid Pin", "Bearer");
        }

        String userHash = pinChallenge.getUserHash();
        Optional<User> user = userDAO.get(userHash);

        if (user.isEmpty()) {
            User newUser = new User(userHash, false);
            userDAO.add(newUser);
        } else if (user.get().isBanned()) {
            throw new ForbiddenException("User is banned");
        }

        String encodedToken = CryptoService.getInstance().generateToken(userHash, isAdmin);
        return new LoginResponse(encodedToken);
    }

    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AuthGuard
    public ApiOk logout(@HeaderParam("Authorization") String bearer) {
        // If @AuthGuard was successful, the token will be present.
        String token = bearer.substring("Bearer ".length());

        CryptoService.getInstance().revokeToken(token);

        return new ApiOk("Token revoked");
    }

    record SendPinBody(String email) {}
    record SendPinResponse(int challengeId) {}
    record LoginBody(Integer challengeId, String pin) {}
    record LoginResponse(String token) {}
}
