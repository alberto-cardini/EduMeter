package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.business_logic.auth.annotations.AdminGuard;
import com.swe.EduMeter.business_logic.auth.annotations.AuthGuard;
import com.swe.EduMeter.model.Report;
import com.swe.EduMeter.orm.ReportDAO;
import com.swe.EduMeter.orm.UserDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;

@Path("/report")
public class ReportController {
    private final ReportDAO reportDAO;
    private final UserDAO userDAO;

    @Inject
    public ReportController(ReportDAO reportDAO, UserDAO userDAO) {
        this.reportDAO = reportDAO;
        this.userDAO = userDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public List<Report> getAll() {
        return reportDAO.getAll();
    }

    @GET
    @Path("/{report_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public Report get(@PathParam("report_id") int reportId) {
        return reportDAO.get(reportId).orElseThrow(() -> new NotFoundException("Report not found"));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AuthGuard
    public CreateResponse create(@Context SecurityContext securityContext,
                      Report report) {
        String userHash = securityContext.getUserPrincipal().getName();
        report.setIssuerHash(userHash);

        return new CreateResponse(reportDAO.add(report));
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{report_id}")
    @AdminGuard
    public void acceptReport(@PathParam("report_id") int reportId,
                             @QueryParam("decision") Boolean decision) {
        reportDAO.get(reportId)
                .map(report -> {
                    return userDAO.get(report.getIssuerHash())
                            .map(user -> {
                                if (decision) user.setBanned(true);
                                return null;
                            })
                            .orElseThrow(() -> new NotFoundException("User not found"));
                })
                .orElseThrow(() -> new NotFoundException("Report not found"));
    }

    private record CreateResponse(int id) {}
}
