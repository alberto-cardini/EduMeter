package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.business_logic.auth.annotations.AdminGuard;
import com.swe.EduMeter.business_logic.auth.annotations.AuthGuard;
import com.swe.EduMeter.model.Report;
import com.swe.EduMeter.orm.ReportDAO;
import com.swe.EduMeter.orm.ReviewDAO;
import com.swe.EduMeter.orm.UserDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;

@Path("/report")
public class ReportController
{
    private final ReportDAO reportDAO;
    private final ReviewDAO reviewDAO;
    private final UserDAO userDAO;

    @Inject
    public ReportController(ReportDAO reportDAO,
                            ReviewDAO reviewDAO,
                            UserDAO userDAO)
    {
        this.reportDAO = reportDAO;
        this.reviewDAO = reviewDAO;
        this.userDAO = userDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public List<Report> getAll()
    {
        return reportDAO.getAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{report_id}")
    @AdminGuard
    public Report get(@PathParam("report_id") int report_id)
    {
        return reportDAO.get(report_id).orElseThrow(() -> new NotFoundException("Report not found"));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @AuthGuard
    public int create(@Context SecurityContext securityContext,
                      @QueryParam("review_id") int review_id) {
        String userHash = securityContext.getUserPrincipal().getName();
        return reportDAO.create(new Report(null, userHash, review_id ));
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{report_id}")
    @AdminGuard
    public void acceptReport(@PathParam("report_id") int report_id,
                             @QueryParam("decision") Boolean decision) {
        reportDAO.get(report_id)
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

}
