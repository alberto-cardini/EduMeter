package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.User;
import com.swe.EduMeter.orm.UserDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;

@Path("/user")
public class UserController {
    private final UserDAO userDAO;
    @Inject
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /* Legacy

    @GET
    @Path("/{userHash}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByHash(@PathParam("userHash") String userHash) {
        return userDAO.getUserByHash(userHash)
                .map(user -> Response.ok(user).build())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
     */

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @GET
    @Path("/profile")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProfile() {
        String dummy_hash = "PROVA1";    // TODO: replace this with the authentication procedure.
        return userDAO.getUserByHash(dummy_hash)
                .map(user -> Response.ok(user).build())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @GET
    @Path("/{user_hash}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserByHash(@PathParam("user_hash") String user_hash) {
        return userDAO.getUserByHash(user_hash).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<User> searchUser(@QueryParam("banned") @DefaultValue("false") boolean banned) {
        return userDAO.getUsersFilteredForBan(banned);
    }

    @POST
    @Path("/{user_hash}/ban")
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean banUser(@PathParam("user_hash") String user_hash) {
        return userDAO.getUserByHash(user_hash)
                .map(user -> {user.setBanned(true); return true;})
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

}
