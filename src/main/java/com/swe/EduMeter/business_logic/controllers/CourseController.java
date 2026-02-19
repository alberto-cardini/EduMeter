package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.business_logic.auth.annotations.AdminGuard;
import com.swe.EduMeter.model.Course;
import com.swe.EduMeter.orm.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/course")
public class CourseController
{
    private final CourseDAO courseDAO;

    @Inject
    public CourseController(CourseDAO courseDAO)
    {
        this.courseDAO = courseDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Course> search(@QueryParam("school_id") Integer school_id,
                               @QueryParam("degree_id") Integer degree_id,
                               @QueryParam("course_name") String course_name)
    {
        return courseDAO.search(course_name, school_id, degree_id);
    }

    @GET
    @Path("/{course_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Course get(@PathParam("course_id") int id)
    {
        return courseDAO.get(id).orElseThrow(() -> new NotFoundException("Course not found"));
    }

/*
    accepted JSON:
    * {
    *    "name" : "string",
    *    "degreeId" : "int"
    * }
*/

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @AdminGuard
    public int create(Course new_course)
    {
        return courseDAO.add(new_course);
    }

    @DELETE
    @Path("/{course_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public Course delete(@PathParam("course_id") int course_id)
    {
        return courseDAO.get(course_id)
                .map(course -> {courseDAO.delete(course_id);
                                return course;})
                .orElseThrow(() -> new NotFoundException("Course not found"));
    }

}
