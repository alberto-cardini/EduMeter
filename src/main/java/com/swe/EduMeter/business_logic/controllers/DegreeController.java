package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.orm.DegreeDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/degree")
public class DegreeController {
    private final DegreeDAO degreeDAO;

    @Inject
    public DegreeController(DegreeDAO degreeDAO) {
        this.degreeDAO = degreeDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDegrees() {
        return Response.ok(degreeDAO.getAllDegrees()).build();
    }
}
