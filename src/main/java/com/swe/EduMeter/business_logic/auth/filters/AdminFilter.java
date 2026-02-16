package com.swe.EduMeter.business_logic.auth.filters;

import com.swe.EduMeter.business_logic.auth.annotations.AdminGuard;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@AdminGuard
@Provider
public class AdminFilter extends AuthFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        super.filter(requestContext);

        if (!requestContext.getSecurityContext().isUserInRole("ADMIN")) {
            throw new ForbiddenException("Forbidden");
        }
    }
}
