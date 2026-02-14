package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.School;
import com.swe.EduMeter.orm.SchoolDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/school")
public class SchoolController {
    private final SchoolDAO schoolDAO;

    @Inject
    public SchoolController(SchoolDAO schoolDAO) {
        this.schoolDAO = schoolDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSchools() {
        return Response.ok(schoolDAO.getAllSchools()).build();
    }

}