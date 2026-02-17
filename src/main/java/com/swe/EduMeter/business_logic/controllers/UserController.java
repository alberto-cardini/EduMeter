package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.User;
import com.swe.EduMeter.orm.UserDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/user")
public class UserController
{
    private final UserDAO userDAO;
    @Inject
    public UserController(UserDAO userDAO)
    {
        this.userDAO = userDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> search(@QueryParam("banned") Boolean banned)
    {
        return userDAO.search(banned);
    }

    @GET
    @Path("/{user_hash}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getByHash(@PathParam("user_hash") String user_hash)
    {
        return userDAO.getByHash(user_hash).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @POST
    @Path("/{user_hash}/ban")
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean ban(@PathParam("user_hash") String user_hash)
    {
        return userDAO.getByHash(user_hash)
                .map(user -> {user.setBanned(true); return true;})
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

}
