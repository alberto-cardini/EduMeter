package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.School;
import com.swe.EduMeter.orm.SchoolDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/school")
public class SchoolController {
    private final SchoolDAO schoolDAO;
    @Inject
    public SchoolController(SchoolDAO schoolDAO) {
        this.schoolDAO = schoolDAO;
    }

    @GET
    @Path("name={schoolName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSchoolByName(@PathParam("schoolName") String schoolName) {

        Response notFoundResponse = Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "not found"))
                .build();

        return schoolDAO.getSchoolByName(schoolName)
                .map(school -> Response.ok(school).build())
                .orElse(notFoundResponse);
    }

    @GET
    @Path("id={schoolId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSchoolByName(@PathParam("schoolId") int id) {

        Response notFoundResponse = Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "not found"))
                .build();

        return schoolDAO.getSchoolById(id)
                .map(school -> Response.ok(school).build())
                .orElse(notFoundResponse);
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSchool(School school){
        schoolDAO.addSchool(school);
        return Response.status(Response.Status.CREATED)
                .entity(school)
                .build();

    }

    @DELETE
    @Path("/id={schoolId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSchool(@PathParam("schoolId") int id) {
        Response notFoundResponse = Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "not found"))
                .build();

        // the first getSchoolById(Id) is needed in order to find if the school with such Id exists or not, if it does,
        // then it is deleted. Otherwise, not found error is returned.
        return schoolDAO.getSchoolById(id)
                .map(school -> {schoolDAO.deleteSchoolById(id); return Response.ok(school).build();})
                .orElse(notFoundResponse);
    }

    @DELETE
    @Path("/name={schoolName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSchool(@PathParam("schoolName") String name) {
        Response notFoundResponse = Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "not found"))
                .build();

        return schoolDAO.getSchoolByName(name)
                .map(school -> {schoolDAO.deleteSchoolByName(name); return Response.ok(school).build();})
                .orElse(notFoundResponse);
    }
}