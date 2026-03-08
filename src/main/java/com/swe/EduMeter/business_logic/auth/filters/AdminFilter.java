package com.swe.EduMeter.business_logic.auth.filters;

import com.swe.EduMeter.business_logic.auth.annotations.AdminGuard;
import com.swe.EduMeter.orm.dao.UserDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@AdminGuard
@Provider
public class AdminFilter extends AuthFilter {
    @Inject
    public AdminFilter(UserDAO userDAO) {
        super(userDAO);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        super.filter(requestContext);

        if (!requestContext.getSecurityContext().isUserInRole("ADMIN")) {
            throw new ForbiddenException("Forbidden");
        }
    }
}
