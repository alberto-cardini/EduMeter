package com.swe.EduMeter.business_logic.auth.filters;

import com.swe.EduMeter.business_logic.auth.TokenService;
import com.swe.EduMeter.business_logic.auth.annotations.AuthGuard;
import com.swe.EduMeter.model.Token;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.security.Principal;

@AuthGuard
@Provider
public class AuthFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Extract authorization header. A valid header looks like this:
        //     Authorization: Bearer <token>
        String authHeader = requestContext.getHeaderString("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Invalid authentication header");
        }

        // Extract the <token> from the value part of the header
        String tokenString = authHeader.substring("Bearer ".length()).trim();

        Token token = TokenService.getInstance().decodeToken(tokenString);

        SecurityContext securityContext = requestContext.getSecurityContext();
        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return token::userHash;
            }

            @Override
            public boolean isUserInRole(String role) {
                if (role.equals("ADMIN")) {
                    return token.isAdmin();
                }

                // Under the assumption that an admin is
                // not a user
                if (role.equals("BASE")) {
                    return !token.isAdmin();
                }

                return false;
            }

            @Override
            public boolean isSecure() {
                return securityContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return "Bearer";
            }
        });
    }
}
