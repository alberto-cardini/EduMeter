package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.business_logic.auth.annotations.AdminGuard;
import com.swe.EduMeter.business_logic.auth.annotations.AuthGuard;
import com.swe.EduMeter.model.DraftReview;
import com.swe.EduMeter.model.response.ApiObjectCreated;
import com.swe.EduMeter.model.response.ApiOk;
import com.swe.EduMeter.orm.DraftReviewDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

import java.time.LocalDate;
import java.util.List;

@Path("/review/draft")
public class DraftReviewController {
    private final DraftReviewDAO reviewDAO;

    @Inject
    public DraftReviewController(DraftReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public List<DraftReview> getAll() {
        return reviewDAO.getAll();
    }

    @GET
    @Path("/{review_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public DraftReview get(@PathParam("review_id") int id) {
        return reviewDAO.get(id).orElseThrow(() -> new NotFoundException("Review not found"));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AuthGuard
    public ApiObjectCreated publish(DraftReview review, @Context SecurityContext sc) {
        review.setCreatorHash(sc.getUserPrincipal().getName());
        review.setDate(LocalDate.now());

        int reviewId = reviewDAO.add(review);

        return new ApiObjectCreated(reviewId, "Published review");
    }

    @DELETE
    @Path("/{review_id}")
    @AdminGuard
    public ApiOk delete(@PathParam("review_id") int id) {
        this.get(id);

        reviewDAO.delete(id);

        return new ApiOk("Review deleted");
    }
}
