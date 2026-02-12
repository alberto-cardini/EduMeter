package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.orm.UserDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/user")
public class UserController {
    private final UserDAO userDAO;
    @Inject
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GET
    @Path("/{userHash}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByHash(@PathParam("userHash") String userHash) {
        return userDAO.getUserByHash(userHash)
                .map(user -> Response.ok(user).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                                .entity(Map.of("error", "not found"))
                                .build()
                );
    }
}
