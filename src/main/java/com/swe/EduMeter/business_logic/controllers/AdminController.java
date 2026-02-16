package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.*;
import com.swe.EduMeter.orm.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;

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


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Admin> getAllAdmins() {
        return adminDAO.getAllAdmins();
    }

    @GET
    @Path("/{admin_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Admin getAdminById(@PathParam("admin_id") int admin_id) {
        return adminDAO.getAdminById(admin_id).orElseThrow(() -> new NotFoundException("Admin not found"));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public boolean createAdmin(Admin admin) {
        return adminDAO.addAdmin(admin);
    }

    @DELETE
    @Path("/{admin_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean deleteAdmin(@PathParam("admin_id") int admin_id) {
        return adminDAO.getAdminById(admin_id)
                .map(a -> {adminDAO.deleteAdminById(admin_id); return true;})
                .orElseThrow(() -> new NotFoundException("Admin not found"));
    }
}