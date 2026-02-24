package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.business_logic.auth.annotations.AdminGuard;
import com.swe.EduMeter.model.Professor;
import com.swe.EduMeter.model.Teaching;
import com.swe.EduMeter.model.response.ApiObjectCreated;
import com.swe.EduMeter.model.response.ApiOk;
import com.swe.EduMeter.orm.ProfDAO;
import com.swe.EduMeter.orm.TeachingDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/prof")
public class ProfessorController {
    private final ProfDAO profDAO;
    private final TeachingDAO teachingDAO;

    @Inject
    public ProfessorController(ProfDAO profDAO, TeachingDAO teachingDAO) {
        this.profDAO = profDAO;
        this.teachingDAO = teachingDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Professor> search(@QueryParam("query") String query,
                                  @QueryParam("course_id") Integer id) {
        return profDAO.search(query, id);
    }

    @GET
    @Path("/{prof_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Professor get(@PathParam("prof_id") int id) {
        return profDAO.get(id).orElseThrow(() -> new NotFoundException("Professor not found"));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public ApiObjectCreated create(Professor prof) {
        return new ApiObjectCreated(profDAO.add(prof), "Professor created");
    }

    @DELETE
    @Path("/{prof_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public ApiOk delete(@PathParam("prof_id") int id) {
        this.get(id);
        profDAO.delete(id);

        return new ApiOk("Professor deleted");
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiOk update(Professor prof) {
        if (prof.getId() == null) {
            throw new BadRequestException("Id must be set");
        }

        this.get(prof.getId());
        profDAO.update(prof);

        return new ApiOk("Professor updated");
    }

    @GET
    @Path("/{prof_id}/teaching")
    @Produces
    public List<Teaching> listTeachings(@PathParam("prof_id") int profId) {
        return teachingDAO.getByProf(profId);
    }
}
