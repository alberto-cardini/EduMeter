package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.business_logic.auth.annotations.AdminGuard;
import com.swe.EduMeter.model.School;
import com.swe.EduMeter.model.response.ApiOk;
import com.swe.EduMeter.orm.SchoolDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/school")
public class SchoolController {
    private final SchoolDAO schoolDAO;

    @Inject
    public SchoolController(SchoolDAO schoolDAO) {
        this.schoolDAO = schoolDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<School> search(@QueryParam("query") String query) {
        return schoolDAO.search(query);
    }

    @GET
    @Path("/{school_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public School get(@PathParam("school_id") int id) {
        return schoolDAO.get(id).orElseThrow(() -> new NotFoundException("School not found"));
    }

    // accepted JSON:
    // {
    //   "name" : "name of the school"
    // }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //@AdminGuard
    public CreateResponse create(School newSchool) {
        return new CreateResponse(schoolDAO.add(newSchool));
    }

    @DELETE
    @Path("/{school_id}")
    @Produces(MediaType.APPLICATION_JSON)
    //@AdminGuard
    public ApiOk delete(@PathParam("school_id") int id) {
        // Finds if the school with such id exists or not,
        // by calling the GET endpoint. If it does, then it
        // is deleted.
        this.get(id);
        schoolDAO.delete(id);

        return new ApiOk("School deleted");
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //@AdminGuard
    public ApiOk update(School school) {
        if (school.getId() == null) {
            throw new BadRequestException("Id must be set");
        }

        // Finds if the school with such id exists or not,
        // by calling the GET endpoint. If it does, then it
        // is updated.
        this.get(school.getId());
        schoolDAO.update(school);

        return new ApiOk("School updated");
    }

    private record CreateResponse(int id) {}
}