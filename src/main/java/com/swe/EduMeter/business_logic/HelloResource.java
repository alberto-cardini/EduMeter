package com.swe.EduMeter.business_logic;

import com.swe.EduMeter.orm.UserDAO;
import com.swe.EduMeter.orm.in_mem.InMemUserDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/hello")
public class HelloResource {
    private final UserDAO userDAO;

    @Inject
    public HelloResource(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    @GET
    @Produces("text/plain")
    public String hello() {
        if (this.userDAO instanceof InMemUserDAO) {
            return "Dependency Injected!";
        }
        return "Hello, World!";
    }
}