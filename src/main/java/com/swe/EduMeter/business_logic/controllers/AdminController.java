package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.School;
import com.swe.EduMeter.orm.AdminDAO;
import com.swe.EduMeter.orm.SchoolDAO;
import com.swe.EduMeter.orm.UserDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/admin")
public class AdminController {
    private final AdminDAO adminDAO;
    private final UserDAO userDAO;
    private final SchoolDAO schoolDAO;

    @Inject
    public AdminController(AdminDAO adminDAO, UserDAO userDAO, SchoolDAO schoolDAO) {
        this.adminDAO = adminDAO;
        this.userDAO = userDAO;
        this.schoolDAO = schoolDAO;
    }

    /* Legacy
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAdmins() {
        return Response.ok(adminDAO.getAllAdmins()).build();
    } */

    @GET
    @Path("/{adminId}/users?banned")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers(@QueryParam("banned") @DefaultValue("false") boolean banned) {
        return Response.ok(userDAO.getAllUsers()).build();
    }

    @GET
    @Path("/{adminId}/users?banned")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBannedUsers(@QueryParam("banned") boolean banned) {
        String admin_dummy_hash = "admin_dummy_hash";
        return (banned) ? Response.ok(userDAO.getAllBannedUsers()).build() : Response.ok(userDAO.getAllUsers()).build();
    }

    @POST
    @Path("/{adminId}/users/{userHash}/ban")
    @Produces(MediaType.APPLICATION_JSON)
    public Response banUserByHash(@PathParam("userHash") String hash) {
        String admin_dummy_hash = "admin_dummy_hash";
        return userDAO.getUserByHash(hash)
                .map(user -> {user.setBanned(true);
                                    return Response.ok(user).build();})
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @DELETE
    @Path("/{adminId}/school/id={schoolId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSchool(@PathParam("schoolId") int id) {
        // the first getSchoolById(Id) is needed in order to find if the school with such Id exists or not, if it does,
        // then it is deleted. Otherwise, not found error is returned.
        return schoolDAO.getSchoolById(id)
                .map(school -> {schoolDAO.deleteSchoolById(id);
                                       return Response.ok(school).build();})
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @DELETE
    @Path("/{adminId}/school/{schoolName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSchool(@PathParam("schoolName") String name) {
        return schoolDAO.getSchoolByName(name)
                .map(school -> {schoolDAO.deleteSchoolByName(name);
                    return Response.ok(school).build();})
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    // accepted JSON: {"name" : "name of the school"}
    @POST
    @Path("/{adminId}/school")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSchool(School school){
        schoolDAO.addSchool(school);
        return Response.status(Response.Status.CREATED).entity(school).build();
    }

    @GET
    @Path("/{adminId}/school/{schoolName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSchoolByName(@PathParam("schoolName") String schoolName) {
        return schoolDAO.getSchoolByName(schoolName)
                .map(school -> Response.ok(school).build())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @GET
    @Path("/{adminId}/school/{schoolId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSchoolByName(@PathParam("schoolId") int id) {
        return schoolDAO.getSchoolById(id)
                .map(school -> Response.ok(school).build())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @GET
    @Path("/{adminId}/school")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSchools() {
        return Response.ok(schoolDAO.getAllSchools()).build();
    }

}
