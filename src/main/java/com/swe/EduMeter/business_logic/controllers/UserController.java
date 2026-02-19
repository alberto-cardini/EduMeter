package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.business_logic.auth.annotations.AdminGuard;
import com.swe.EduMeter.business_logic.auth.annotations.AuthGuard;
import com.swe.EduMeter.model.User;
import com.swe.EduMeter.orm.UserDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

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
    @AdminGuard
    public List<User> search(@QueryParam("banned") Boolean banned)
    {
        return userDAO.search(banned);
    }

    @GET
    @Path("/profile")
    @Produces(MediaType.APPLICATION_JSON)
    @AuthGuard
    public User getByHash(@Context SecurityContext sc)
    {
        String user_hash = sc.getUserPrincipal().getName();
        System.out.println(user_hash);
        return userDAO.getByHash(user_hash).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @POST
    @Path("/{user_hash}/ban")
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public Boolean ban(@PathParam("user_hash") String user_hash)
    {
        return userDAO.getByHash(user_hash)
                .map(user -> {user.setBanned(true); return true;})
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

}
