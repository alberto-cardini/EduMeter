package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.Course;
import com.swe.EduMeter.model.Degree;
import com.swe.EduMeter.orm.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;

@Path("/degree")
public class DegreeController {
    private final SchoolDAO schoolDAO;
    private final DegreeDAO degreeDAO;
    private final CourseDAO courseDAO;

    @Inject
    public DegreeController(DegreeDAO degreeDAO, CourseDAO courseDAO, SchoolDAO schoolDAO) {
        this.schoolDAO = schoolDAO;
        this.degreeDAO = degreeDAO;
        this.courseDAO = courseDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Degree> getAllDegrees() { return degreeDAO.getAllDegrees(); }

    @GET
    @Path("/{degree_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Degree getDegree(@PathParam("degree_name") String degree_name) {
        return degreeDAO.getDegreeByName(degree_name).orElseThrow(() -> new NotFoundException("Degree not found"));
    }

    @GET
    @Path("/id={degree_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Degree getDegree(@PathParam("degree_id") int degree_id) {
        return degreeDAO.getDegreeById(degree_id).orElseThrow(() -> new NotFoundException("Degree not found"));
    }

    @GET
    @Path("/{degree_name}/courses")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Course> getAllCoursesInDegree(@PathParam("degree_name") String degree_name) {
        return degreeDAO.getDegreeByName(degree_name)
                        .map(degree -> courseDAO.getAllCoursesByDegree(degree_name))
                        .orElseThrow(() -> new NotFoundException("Degree not found"));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean createDegree(Degree new_degree) {

        if(schoolDAO.getSchoolByName(new_degree.getSchool().getName()).isEmpty()) {
            schoolDAO.addSchool(new_degree.getSchool());
        }

        if(degreeDAO.getDegreeByName(new_degree.getName()).isEmpty()) {
            degreeDAO.addDegree(new_degree);
            return true;
        }
        return false;
    }

    @DELETE
    @Path("{degree_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean deleteDegree(@PathParam("degree_name") String degree_name) {
        return degreeDAO.getDegreeByName(degree_name)
                .map(degree -> {courseDAO.getAllCoursesByDegree(degree.getName())
                        .forEach(course -> courseDAO.deleteCourseByName(course.getName()));
                        return degreeDAO.deleteDegreeByName(degree_name);})
                .orElseThrow(() -> new NotFoundException("School not found"));
    }

}
