package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.business_logic.auth.annotations.AdminGuard;
import com.swe.EduMeter.model.*;
import com.swe.EduMeter.orm.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/admin")
public class AdminController {
    private final AdminDAO adminDAO;

    @Inject
    public AdminController(AdminDAO adminDAO)
    {
        this.adminDAO = adminDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public List<Admin> getAll()
    {
        return adminDAO.getAll();
    }

    @GET
    @Path("/{admin_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public Admin getById(@PathParam("admin_id") int admin_id)
    {
        return adminDAO.get(admin_id).orElseThrow(() -> new NotFoundException("Admin not found"));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public boolean create(Admin admin)
    {
        return adminDAO.add(admin);
    }

    @DELETE
    @Path("/{admin_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public boolean delete(@PathParam("admin_id") int admin_id)
    {
        return adminDAO.get(admin_id)
                .map(a -> {adminDAO.delete(admin_id); return true;})
                .orElseThrow(() -> new NotFoundException("Admin not found"));
    }
}