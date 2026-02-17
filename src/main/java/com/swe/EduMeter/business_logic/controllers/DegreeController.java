package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.Degree;
import com.swe.EduMeter.orm.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/degree")
public class DegreeController
{
    private final DegreeDAO degreeDAO;

    @Inject
    public DegreeController(DegreeDAO degreeDAO)
    {
        this.degreeDAO = degreeDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Degree> search(@QueryParam("school_id") int id,
                               @QueryParam("degree_name") String name)
    {
        return degreeDAO.search(name, id);
    }

    @GET
    @Path("/id={degree_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Degree getDegree(@PathParam("degree_id") int degree_id)
    {
        return degreeDAO.get(degree_id).orElseThrow(() -> new NotFoundException("Degree not found"));
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public int create(Degree new_degree)
    {
        return degreeDAO.add(new_degree);
    }

    @DELETE
    @Path("/{degree_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Degree delete(@PathParam("degree_id") int degree_id)
    {
        return degreeDAO.get(degree_id)
                .map(degree -> {degreeDAO.delete(degree_id);
                        return degree;})
                .orElseThrow(() -> new NotFoundException("School not found"));
    }

}
