package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.business_logic.auth.CryptoService;
import com.swe.EduMeter.business_logic.auth.annotations.AuthGuard;
import com.swe.EduMeter.model.Pin;
import com.swe.EduMeter.model.response.ApiOk;
import com.swe.EduMeter.orm.AdminDAO;
import com.swe.EduMeter.orm.PinDAO;
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


@Path("/auth")
public class AuthController {
    public static final String[]  WHITELISTED_DOMAINS = { "edu.unifi.it" };
    public static final int PIN_EXPIRATION_MIN = 10;
    private final PinDAO pinDAO;
    private final AdminDAO adminDAO;

    @Inject
    public AuthController(PinDAO pinDAO, AdminDAO adminDAO) {
        this.pinDAO = pinDAO;
        this.adminDAO = adminDAO;
    }

    @POST
    @Path("/sendPin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiOk sendPin(@QueryParam("admin") boolean isAdmin,
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
            Pin pin = new Pin(null, veryRandomPin, userHash, expiresAt, true);

            pinDAO.add(pin);
            // TODO: send pin

            return new ApiOk("Pin sent");
        }

        for (String domain: WHITELISTED_DOMAINS) {
            if (body.email().endsWith("@" + domain)) {
                String veryRandomPin = "1234";
                String userHash = CryptoService.getInstance().getUserId(body.email());
                Instant expiresAt = Instant.now().plus(Duration.ofMinutes(PIN_EXPIRATION_MIN));
                Pin pin = new Pin(null, veryRandomPin, userHash, expiresAt, false);

                pinDAO.add(pin);
                // TODO: send pin

                return new ApiOk("Pin sent");
            }
        }

        throw new ForbiddenException("Invalid email domain");
    }

    /*
     * MOCK METHOD, which can be used for testing and whatnot.
     * It always returns a valid token, despite the PIN sent.
     * The userhash is the same as the received email.
     */
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public LoginResponse login(@QueryParam("admin") boolean isAdmin,
                               LoginBody body) {
        if (body.pin() == null) {
            throw new BadRequestException("Missing field 'pin'");
        }
        if (body.email() == null) {
            throw new BadRequestException("Missing field 'email'");
        }

        String userHash = CryptoService.getInstance().getUserId(body.email());
        Pin expectedPin = pinDAO.get(userHash, isAdmin)
                                .orElseThrow(() -> new NotFoundException("Invalid or expired Pin"));

        if (expectedPin.getPin().equals(body.pin())) {
            String encodedToken =  CryptoService.getInstance().generateToken(body.email(), isAdmin);
            return new LoginResponse(encodedToken);
        }

        throw new NotAuthorizedException("Invalid Pin");
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

    private record SendPinBody(String email) {}
    private record LoginBody(String email, String pin) {}
    private record LoginResponse(String token) {}
}
