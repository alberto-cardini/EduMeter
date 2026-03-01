package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.business_logic.auth.annotations.AdminGuard;
import com.swe.EduMeter.business_logic.auth.annotations.AuthGuard;
import com.swe.EduMeter.model.Report;
import com.swe.EduMeter.model.response.ApiObjectCreated;
import com.swe.EduMeter.model.response.ApiOk;
import com.swe.EduMeter.orm.PublishedReviewDAO;
import com.swe.EduMeter.orm.ReportDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;

@Path("/report")
public class ReportController {
    private final ReportDAO reportDAO;
    private final PublishedReviewDAO reviewDAO;

    @Inject
    public ReportController(ReportDAO reportDAO, PublishedReviewDAO reviewDAO) {
        this.reportDAO = reportDAO;
        this.reviewDAO = reviewDAO;
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
    public ApiObjectCreated create(@Context SecurityContext securityContext,
                                   Report report) {
        String userHash = securityContext.getUserPrincipal().getName();
        report.setIssuerHash(userHash);

        int id = reportDAO.add(report);

        return new ApiObjectCreated(id, "Report created");
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{report_id}")
    @AdminGuard
    public ApiOk delete(@PathParam("report_id") int reportId) {
        this.get(reportId);

        reportDAO.delete(reportId);

        return new ApiOk("Report deleted");
    }

    @POST
    @Path("/{report_id}/approve")
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public ApiOk acceptReport(@PathParam("report_id") int reportId) {
        Report r = this.get(reportId);
        this.delete(reportId);

        reviewDAO.delete(r.getReviewId());

        return new ApiOk("Report approved");
    }
}
