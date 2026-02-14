package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.User;
import com.swe.EduMeter.orm.UserDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;
import java.util.Optional;

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
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
