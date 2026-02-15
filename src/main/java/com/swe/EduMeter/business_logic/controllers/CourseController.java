package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.orm.CourseDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class CourseController {
    private final CourseDAO courseDAO;

    @Inject
    public CourseController(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCourses() {
        return Response.ok(courseDAO.getAllCourses()).build();
    }
}
