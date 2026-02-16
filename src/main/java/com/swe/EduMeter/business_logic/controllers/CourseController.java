package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.Course;
import com.swe.EduMeter.orm.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;

@Path("/course")
public class CourseController {
    private final CourseDAO courseDAO;
    private final SchoolDAO schoolDAO;
    private final DegreeDAO degreeDAO;

    @Inject
    public CourseController(SchoolDAO schoolDAO, DegreeDAO degreeDAO, CourseDAO courseDAO) {
        this.schoolDAO = schoolDAO;
        this.degreeDAO = degreeDAO;
        this.courseDAO = courseDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Course> getAllCourses() {
        return courseDAO.getAllCourses();
    }

    @GET
    @Path("/{course_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Course getCourseByName(@PathParam("course_name") String course_name) {
        return courseDAO.getCourseByName(course_name)
                .orElseThrow(() -> new NotFoundException("Course not found"));
    }

    @GET
    @Path("/id={courseId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Course getCourseByName(@PathParam("courseId") int id) {
        return courseDAO.getCourseById(id)
                .orElseThrow(() -> new NotFoundException("Course not found"));
    }

/*
    accepted JSON:
            * {
    *    "name" : "string",
    *    "degree" : {
    *                  "name" : "degree-name",
    *                  "type" : Bachelor/Master,
    *                  "school" : {
    *                                "name" : "school name",
    *                             }
    *               }
    * }
*/

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean createCourse(Course new_course){

        if(schoolDAO.getSchoolByName(new_course.getDegree().getSchool().getName()).isEmpty()) {
            schoolDAO.addSchool(new_course.getDegree().getSchool());
        }

        if(degreeDAO.getDegreeByName(new_course.getDegree().getName()).isEmpty()) {
            degreeDAO.addDegree(new_course.getDegree());
        }

        if(courseDAO.getCourseByName(new_course.getName()).isEmpty()) {
            courseDAO.addCourse(new_course);
            return true;
        }
        return false;
    }

    @DELETE
    @Path("/{course_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean deleteCourse(@PathParam("course_name") String course_name) {
        return courseDAO.getCourseByName(course_name)
                .map(course -> courseDAO.deleteCourseByName(course.getName()))
                .orElseThrow(() -> new NotFoundException("Course not found"));
    }

}
