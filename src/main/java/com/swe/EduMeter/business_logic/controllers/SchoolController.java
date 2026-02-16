package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.Course;
import com.swe.EduMeter.model.Degree;
import com.swe.EduMeter.model.School;
import com.swe.EduMeter.orm.CourseDAO;
import com.swe.EduMeter.orm.DegreeDAO;
import com.swe.EduMeter.orm.SchoolDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/school")
public class SchoolController {
    private final SchoolDAO schoolDAO;
    private final DegreeDAO degreeDAO;
    private final CourseDAO courseDAO;

    @Inject
    public SchoolController(SchoolDAO schoolDAO, DegreeDAO degreeDAO, CourseDAO courseDAO) {
        this.schoolDAO = schoolDAO;
        this.degreeDAO = degreeDAO;
        this.courseDAO = courseDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<School> getAllSchools() { return schoolDAO.getAllSchools(); }

    @GET
    @Path("/{schoolName}")
    @Produces(MediaType.APPLICATION_JSON)
    public School getSchoolByName(@PathParam("schoolName") String schoolName) {
        return schoolDAO.getSchoolByName(schoolName).orElseThrow(() -> new NotFoundException("School not found"));
    }

    @GET
    @Path("/id={schoolId}")
    @Produces(MediaType.APPLICATION_JSON)
    public School getSchoolByName(@PathParam("schoolId") int id) {
        return schoolDAO.getSchoolById(id).orElseThrow(() -> new NotFoundException("School not found"));
    }

    @GET
    @Path("/{school_name}/degrees")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Degree> getAllDegrees(@PathParam("school_name") String school_name) {
        return schoolDAO.getSchoolByName(school_name)
                .map(school -> degreeDAO.getAllDegreesBySchool(school.getName()))
                .orElseThrow(() -> new NotFoundException("School not found"));
    }

    @GET
    @Path("/{school_name}/{degree_name}/courses")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Course> getAllCoursesInDegree(@PathParam("school_name") String school_name,
                                                   @PathParam("degree_name") String degree_name) {
        return schoolDAO.getSchoolByName(school_name)
                .map(school -> degreeDAO.getDegreeByName(degree_name)
                        .map(degree -> courseDAO.getAllCoursesByDegree(degree_name))
                        .orElseThrow(() -> new NotFoundException("Degree not found")))
                .orElseThrow(() -> new NotFoundException("School not found"));
    }

    // accepted JSON:
    // {
    //   "name" : "name of the school"
    // }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSchool(School school){
        schoolDAO.addSchool(school);
        return Response.status(Response.Status.CREATED).entity(school).build();
    }

    @DELETE
    @Path("/id={schoolId}")
    @Produces(MediaType.APPLICATION_JSON)
    public School deleteSchool(@PathParam("schoolId") int id) {
        // the first getSchoolById(Id) is needed in order to find if the school with such Id exists or not, if it does,
        // then it is deleted. Otherwise, not found error is returned.
        return schoolDAO.getSchoolById(id)
                .map(school -> {schoolDAO.deleteSchoolById(id); return school;})
                .orElseThrow(() -> new NotFoundException("School not found"));
    }

    @DELETE
    @Path("/{schoolName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean deleteSchool(@PathParam("schoolName") String name) {
        return schoolDAO.getSchoolByName(name)
                .map(school -> {
                    degreeDAO.getAllDegreesBySchool(school.getName())
                            .forEach(degree -> {courseDAO.deleteAllCoursesByDegree(degree.getName());});
                    degreeDAO.deleteAllDegreesBySchool(school.getName());
                    return schoolDAO.deleteSchoolByName(name);})
                .orElseThrow(() -> new NotFoundException("School not found"));
    }

}