package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.*;
import com.swe.EduMeter.orm.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/admin")
public class AdminController {
    private final AdminDAO adminDAO;
    private final UserDAO userDAO;
    private final SchoolDAO schoolDAO;
    private final DegreeDAO degreeDAO;
    private final CourseDAO courseDAO;

    @Inject
    public AdminController(AdminDAO adminDAO, UserDAO userDAO, SchoolDAO schoolDAO, DegreeDAO degreeDAO, CourseDAO courseDAO) {
        this.adminDAO = adminDAO;
        this.userDAO = userDAO;
        this.schoolDAO = schoolDAO;
        this.degreeDAO = degreeDAO;
        this.courseDAO = courseDAO;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAdmins() {
        return Response.ok(adminDAO.getAllAdmins()).build();
    }
/*
    @GET
    @Path("/{adminId}/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers(@PathParam("adminId") String adminId) {
        return Response.ok(userDAO.getAllUsers()).build();
    }

    @GET
    @Path("/{adminId}/users?banned")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBannedUsers(@QueryParam("banned") @DefaultValue("false") boolean banned,
                                      @PathParam("adminId") String adminId) {
        String admin_dummy_hash = "admin_dummy_hash";
        return (banned) ? Response.ok(userDAO.getAllBannedUsers()).build() : Response.ok(userDAO.getAllUsers()).build();
    }

    @POST
    @Path("/{adminId}/users/{userHash}/ban")
    @Produces(MediaType.APPLICATION_JSON)
    public Response banUserByHash(@PathParam("userHash") String hash,
                                  @PathParam("adminId") String adminId) {
        String admin_dummy_hash = "admin_dummy_hash";
        return userDAO.getUserByHash(hash)
                .map(user -> {user.setBanned(true);
                                    return Response.ok(user).build();})
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
     */
}