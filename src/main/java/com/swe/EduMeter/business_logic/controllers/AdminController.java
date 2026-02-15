package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.*;
import com.swe.EduMeter.orm.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/admin")
public class AdminController {
    private final AdminDAO adminDAO;
    private final UserDAO userDAO;
    private final SchoolDAO schoolDAO;
    private final DegreeDAO degreeDAO;
    private final CourseDAO courseDAO;

    @Inject
    public AdminController(AdminDAO adminDAO, UserDAO userDAO, SchoolDAO schoolDAO, DegreeDAO degreeDAO, CourseDAO courseDAO) {
        this.adminDAO = adminDAO;
        this.userDAO = userDAO;
        this.schoolDAO = schoolDAO;
        this.degreeDAO = degreeDAO;
        this.courseDAO = courseDAO;
    }

    /* Legacy
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAdmins() {
        return Response.ok(adminDAO.getAllAdmins()).build();
    } */

    @GET
    @Path("/{adminId}/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers(@PathParam("adminId") String adminId) {
        return Response.ok(userDAO.getAllUsers()).build();
    }

    @GET
    @Path("/{adminId}/users?banned")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBannedUsers(@QueryParam("banned") @DefaultValue("false") boolean banned,
                                      @PathParam("adminId") String adminId) {
        String admin_dummy_hash = "admin_dummy_hash";
        return (banned) ? Response.ok(userDAO.getAllBannedUsers()).build() : Response.ok(userDAO.getAllUsers()).build();
    }

    @POST
    @Path("/{adminId}/users/{userHash}/ban")
    @Produces(MediaType.APPLICATION_JSON)
    public Response banUserByHash(@PathParam("userHash") String hash,
                                  @PathParam("adminId") String adminId) {
        String admin_dummy_hash = "admin_dummy_hash";
        return userDAO.getUserByHash(hash)
                .map(user -> {user.setBanned(true);
                                    return Response.ok(user).build();})
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @GET
    @Path("/{adminId}/school")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSchools(@PathParam("adminId") String adminId) {
        return Response.ok(schoolDAO.getAllSchools()).build();
    }

    @GET
    @Path("/{adminId}/school/{schoolName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSchoolByName(@PathParam("schoolName") String schoolName,
                                    @PathParam("adminId") String adminId) {
        return schoolDAO.getSchoolByName(schoolName)
                .map(school -> Response.ok(school).build())
                .orElseThrow(() -> new NotFoundException("School not found"));
    }

    @GET
    @Path("/{adminId}/school/{schoolId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSchoolByName(@PathParam("schoolId") int id,
                                    @PathParam("adminId") String adminId) {
        return schoolDAO.getSchoolById(id)
                .map(school -> Response.ok(school).build())
                .orElseThrow(() -> new NotFoundException("School not found"));
    }

    // accepted JSON:
    // {
    //   "name" : "name of the school"
    // }
    @POST
    @Path("/{adminId}/school")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSchool(School school,
                                 @PathParam("adminId") String adminId){
        schoolDAO.addSchool(school);
        return Response.status(Response.Status.CREATED).entity(school).build();
    }

    @DELETE
    @Path("/{adminId}/school/id={schoolId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSchool(@PathParam("schoolId") int id,
                                 @PathParam("adminId") String adminId) {
        // the first getSchoolById(Id) is needed in order to find if the school with such Id exists or not, if it does,
        // then it is deleted. Otherwise, not found error is returned.
        return schoolDAO.getSchoolById(id)
                .map(school -> {schoolDAO.deleteSchoolById(id);
                    return Response.ok(school).build();})
                .orElseThrow(() -> new NotFoundException("School not found"));
    }

    @DELETE
    @Path("/{adminId}/school/{schoolName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSchool(@PathParam("schoolName") String name,
                                 @PathParam("adminId") String adminId) {
        return schoolDAO.getSchoolByName(name)
                .map(school -> {schoolDAO.deleteSchoolByName(name);
                    return Response.ok(school).build();})
                .orElseThrow(() -> new NotFoundException("School not found"));
    }

    @GET
    @Path("/{adminId}/course")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCourses(@PathParam("adminId") String adminId) {
        return Response.ok(courseDAO.getAllCourses()).build();
    }

    @GET
    @Path("/{adminId}/course/{courseName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourseByName(@PathParam("courseName") String courseName,
                                    @PathParam("adminId") String adminId) {
        return courseDAO.getCourseByName(courseName)
                .map(course -> Response.ok(course).build())
                .orElseThrow(() -> new NotFoundException("Course not found"));
    }

    @GET
    @Path("/{adminId}/course/{courseId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourseByName(@PathParam("courseId") int id,
                                    @PathParam("adminId") String adminId) {
        return courseDAO.getCourseById(id)
                .map(course -> Response.ok(course).build())
                .orElseThrow(() -> new NotFoundException("Course not found"));
    }

    /* accepted JSON:
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
    @Path("/{adminId}/course")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCourse(Course course,
                                 @PathParam("adminId") String adminId){
        // TODO: add the guard responsible for checking if the course given as parameter got
        //       a course.degree already present in the DB.
        schoolDAO.addSchool(course.getDegree().getSchool());
        degreeDAO.addDegree(course.getDegree());
        courseDAO.addCourse(course);
        return Response.status(Response.Status.CREATED).entity(course).build();
    }
}