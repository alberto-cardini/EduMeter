package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.business_logic.auth.annotations.AdminGuard;
import com.swe.EduMeter.model.Degree;
import com.swe.EduMeter.model.response.ApiOk;
import com.swe.EduMeter.orm.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/degree")
public class DegreeController {
    private final DegreeDAO degreeDAO;

    @Inject
    public DegreeController(DegreeDAO degreeDAO) {
        this.degreeDAO = degreeDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Degree> search(@QueryParam("query") String query,
                               @QueryParam("school_id") Integer id) {
        return degreeDAO.search(query, id);
    }

    @GET
    @Path("/{degree_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Degree get(@PathParam("degree_id") int id) {
        return degreeDAO.get(id).orElseThrow(() -> new NotFoundException("Degree not found"));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //@AdminGuard
    public CreateResponse create(Degree newDegree) {
        return new CreateResponse(degreeDAO.add(newDegree));
    }

    @DELETE
    @Path("/{degree_id}")
    @Produces(MediaType.APPLICATION_JSON)
    //@AdminGuard
    public ApiOk delete(@PathParam("degree_id") int id) {
        // Finds if the degree with such id exists or not,
        // by calling the GET endpoint. If it does, then it
        // is deleted.
        this.get(id);
        degreeDAO.delete(id);

        return new ApiOk("Degree deleted");
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //@AdminGuard
    public ApiOk update(Degree degree) {
        if (degree.getId() == null) {
            throw new BadRequestException("Id must be set");
        }

        // Finds if the degree with such id exists or not,
        // by calling the GET endpoint. If it does, then it
        // is updated.
        this.get(degree.getId());
        degreeDAO.update(degree);

        return new ApiOk("Degree updated");
    }

    private record CreateResponse(int id) {}
}
