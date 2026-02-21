package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.business_logic.auth.annotations.AdminGuard;
import com.swe.EduMeter.model.*;
import com.swe.EduMeter.model.response.ApiObjectCreated;
import com.swe.EduMeter.model.response.ApiOk;
import com.swe.EduMeter.orm.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/admin")
public class AdminController {
    private final AdminDAO adminDAO;

    @Inject
    public AdminController(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public List<Admin> getAll() {
        return adminDAO.getAll();
    }

    @GET
    @Path("/{admin_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public Admin get(@PathParam("admin_id") int id) {
        return adminDAO.get(id).orElseThrow(() -> new NotFoundException("Admin not found"));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public ApiObjectCreated create(Admin admin) {
        int adminId =  adminDAO.add(admin);

        return new ApiObjectCreated(adminId, "Admin created");
    }

    @DELETE
    @Path("/{admin_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public ApiOk delete(@PathParam("admin_id") int id) {
        this.get(id);
        adminDAO.delete(id);

        return new ApiOk("Admin deleted");
    }
}