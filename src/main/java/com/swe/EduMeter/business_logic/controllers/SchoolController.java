package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.School;
import com.swe.EduMeter.orm.SchoolDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/school")
public class SchoolController
{
    private final SchoolDAO schoolDAO;

    @Inject
    public SchoolController(SchoolDAO schoolDAO)
    {
        this.schoolDAO = schoolDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<School> search(@QueryParam("school_name") String name)
    {
        return schoolDAO.search(name);
    }

    @GET
    @Path("/id={school_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public School get(@PathParam("school_id") int id)
    {
        return schoolDAO.get(id).orElseThrow(() -> new NotFoundException("School not found"));
    }

    // accepted JSON:
    // {
    //   "name" : "name of the school"
    // }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public int create(School new_school)
    {
        return schoolDAO.add(new_school);
    }

    @DELETE
    @Path("/id={school_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public School delete(@PathParam("school_id") int id)
    {
        // the first getSchoolById(Id) is needed in order to find if
        // the school with such Id exists or not, if it does,
        // then it is deleted. Otherwise, not found error is returned.
        return schoolDAO.get(id)
                .map(school -> {schoolDAO.delete(id);
                    return school;})
                .orElseThrow(() -> new NotFoundException("School not found"));
    }

}