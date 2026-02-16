package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.business_logic.auth.TokenService;
import com.swe.EduMeter.business_logic.auth.annotations.AuthGuard;
import com.swe.EduMeter.model.ServerResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;


@Path("/auth")
public class AuthController {
    public static final String[]  WHITELISTED_DOMAINS = { "edu.unifi.it" };

    @POST
    @Path("/sendPin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ServerResponse sendPin(SendPinBody body) {
        if (body.email() == null) {
            throw new BadRequestException("Missing field 'email'");
        }

        for (String domain: WHITELISTED_DOMAINS) {
            if (body.email().endsWith("@" + domain)) {
                // TODO: send pin
                return new ServerResponse("Pin sent");
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
    public LoginResponse login(LoginBody body) {
        if (body.pin() == null) {
            throw new BadRequestException("Missing field 'pin'");
        }
        if (body.email() == null) {
            throw new BadRequestException("Missing field 'email'");
        }

        // TODO: add check
        String encodedToken =  TokenService.getInstance().generateToken("email", false);
        return new LoginResponse(encodedToken);
    }

    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AuthGuard
    public ServerResponse logout(@HeaderParam("Authorization") String bearer) {
        // If @AuthGuard was successful, the token will be present.
        String token = bearer.substring("Bearer ".length());

        TokenService.getInstance().revokeToken(token);

        return new ServerResponse("Token revoked");
    }

    private record SendPinBody(String email) {}
    private record LoginBody(String email, String pin) {}
    private record LoginResponse(String token) {}
}
