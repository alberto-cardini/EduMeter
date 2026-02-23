package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.business_logic.auth.annotations.AdminGuard;
import com.swe.EduMeter.model.User;
import com.swe.EduMeter.model.response.ApiOk;
import com.swe.EduMeter.orm.UserDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/user")
public class UserController {
    private final UserDAO userDAO;
    @Inject
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public List<User> search(@QueryParam("banned") Boolean banned) {
        return userDAO.search(banned);
    }

    @POST
    @Path("/{user_hash}/ban")
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public ApiOk ban(@PathParam("user_hash") String userHash) {
        User u = userDAO.get(userHash)
                .orElseThrow(() -> new NotFoundException("User not found"));
        u.setBanned(true);

        userDAO.update(u);

        return new ApiOk("User banned");
    }

}
