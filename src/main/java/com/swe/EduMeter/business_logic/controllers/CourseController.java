package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.business_logic.auth.annotations.AdminGuard;
import com.swe.EduMeter.model.Course;
import com.swe.EduMeter.model.response.ApiOk;
import com.swe.EduMeter.orm.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/course")
public class CourseController {
    private final CourseDAO courseDAO;

    @Inject
    public CourseController(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Course> search(@QueryParam("query") String query,
                               @QueryParam("school_id") Integer schoolId,
                               @QueryParam("degree_id") Integer degreeId) {
        return courseDAO.search(query, schoolId, degreeId);
    }

    @GET
    @Path("/{course_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Course get(@PathParam("course_id") int id) {
        return courseDAO.get(id).orElseThrow(() -> new NotFoundException("Course not found"));
    }

    /*
     * accepted JSON:
     * {
     *    "name" : "string",
     *    "degree_id" : "int"
     * }
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //@AdminGuard
    public CreateResponse create(Course newCourse) {
        return new CreateResponse(courseDAO.add(newCourse));
    }

    @DELETE
    @Path("/{course_id}")
    @Produces(MediaType.APPLICATION_JSON)
    //@AdminGuard
    public ApiOk delete(@PathParam("course_id") int id) {
        // Finds if the course with such id exists or not,
        // by calling the GET endpoint. If it does, then it
        // is deleted.
        this.get(id);
        courseDAO.delete(id);

        return new ApiOk("Course deleted");
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //@AdminGuard
    public ApiOk update(Course course) {
        if (course.getId() == null) {
            throw new BadRequestException("Id must be set");
        }

        // Finds if the course with such id exists or not,
        // by calling the GET endpoint. If it does, then it
        // is updated.
        this.get(course.getId());
        courseDAO.update(course);

        return new ApiOk("Course updated");
    }

    private record CreateResponse(int id) {}
}
