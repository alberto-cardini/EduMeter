package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.business_logic.auth.annotations.AdminGuard;
import com.swe.EduMeter.model.Course;
import com.swe.EduMeter.model.Teaching;
import com.swe.EduMeter.model.response.ApiObjectCreated;
import com.swe.EduMeter.model.response.ApiOk;
import com.swe.EduMeter.orm.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/course")
public class CourseController {
    private final CourseDAO courseDAO;
    private final TeachingDAO teachingDAO;

    @Inject
    public CourseController(CourseDAO courseDAO, TeachingDAO teachingDAO) {
        this.courseDAO = courseDAO;
        this.teachingDAO = teachingDAO;
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
    @AdminGuard
    public ApiObjectCreated create(Course newCourse) {
        return new ApiObjectCreated(courseDAO.add(newCourse), "Course created");
    }

    @DELETE
    @Path("/{course_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
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
    @AdminGuard
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

    @GET
    @Path("/{course_id}/teaching")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Teaching> listTeachings(@PathParam("course_id") int courseId) {
        return teachingDAO.getByCourse(courseId);
    }

    @POST
    @Path("/{course_id}/teaching")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public ApiObjectCreated addTeaching(@PathParam("course_id") int courseId,
                                        AddTeachingBody body) {
        if (body.profId() == null) {
            throw new BadRequestException("profId must be set");
        }

        int teachingId = teachingDAO.add(new Teaching(null, courseId, body.profId()));

        return new ApiObjectCreated(teachingId, "Added teacher to course");
    }

    @DELETE
    @Path("/{course_id}/teaching/{teaching_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public ApiOk removeTeaching(@PathParam("course_id") int ignored,
                                @PathParam("teaching_id") int teachingId) {
        teachingDAO.get(teachingId).orElseThrow(() -> new NotFoundException("Teaching not found"));
        teachingDAO.delete(teachingId);

        return new ApiOk("Removed teacher from course");
    }

    private record AddTeachingBody(Integer profId) {}
}
