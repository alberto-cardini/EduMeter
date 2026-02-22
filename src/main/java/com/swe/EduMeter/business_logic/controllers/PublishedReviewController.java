package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.business_logic.auth.annotations.AdminGuard;
import com.swe.EduMeter.business_logic.auth.annotations.AuthGuard;
import com.swe.EduMeter.business_logic.auth.filters.AuthFilter;
import com.swe.EduMeter.model.PublishedReview;
import com.swe.EduMeter.model.response.ApiObjectCreated;
import com.swe.EduMeter.model.response.ApiOk;
import com.swe.EduMeter.orm.PublishedReviewDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

import java.time.LocalDate;
import java.util.List;

@Path("/review")
public class PublishedReviewController {

    private final PublishedReviewDAO publishedReviewDAO;

    @Inject
    public PublishedReviewController(PublishedReviewDAO publishedReviewDAO) {
        this.publishedReviewDAO = publishedReviewDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PublishedReview> search(@QueryParam("school_id") Integer schoolId,
                                        @QueryParam("degree_id") Integer degreeId,
                                        @QueryParam("course_id") Integer courseId,
                                        @QueryParam("professor_id") Integer profId,
                                        @Context ContainerRequestContext ctx) {
        AuthFilter filter = new AuthFilter();

        try {
            filter.filter(ctx);
            String userHash = ctx.getSecurityContext().getUserPrincipal().getName();

            return publishedReviewDAO.search(schoolId, degreeId, courseId, profId, userHash);
        } catch (Exception ignored) {}

        return publishedReviewDAO.search(schoolId, degreeId, courseId, profId, null);
    }

    @GET
    @Path("/{review_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public PublishedReview get(@PathParam("review_id") int id,
                               @Context ContainerRequestContext ctx) {
        AuthFilter filter = new AuthFilter();

        try {
            filter.filter(ctx);
            String userHash = ctx.getSecurityContext().getUserPrincipal().getName();

            return publishedReviewDAO.get(id, userHash).orElseThrow(() -> new NotFoundException("Review not found"));
        } catch (Exception ignored) {}

        return publishedReviewDAO.get(id, null).orElseThrow(() -> new NotFoundException("Review not found"));
    }

    @POST
    @AuthGuard
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiObjectCreated publish(PublishedReview review, @Context SecurityContext sc) {
        review.setCreatorHash(sc.getUserPrincipal().getName());
        review.setDate(LocalDate.now());
        review.setUpvotes(0);

        int reviewId = publishedReviewDAO.add(review);

        return new ApiObjectCreated(reviewId, "Published review");
    }

    @POST
    @Path("/{review_id}/vote")
    @AuthGuard
    @Produces(MediaType.APPLICATION_JSON)
    public ApiOk toggleVote(@PathParam("review_id") int id,
                            @Context ContainerRequestContext ctx) {
        // Finds if the review with such id exists or not,
        // by calling the GET endpoint. If it does, then it
        // toggles the upvote for the current user.
        this.get(id, ctx);

        String userHash = ctx.getSecurityContext().getUserPrincipal().getName();
        publishedReviewDAO.toggleUpvote(id, userHash);

        return new ApiOk("Vote toggled");
    }


    @DELETE
    @Path("/{review_id}")
    @AdminGuard
    @Produces(MediaType.APPLICATION_JSON)
    public ApiOk delete(@PathParam("review_id") int id,
                        @Context ContainerRequestContext ctx) {
        // Finds if the review with such id exists or not,
        // by calling the GET endpoint. If it does, then it
        // is deleted.
        this.get(id, ctx);
        publishedReviewDAO.delete(id);

        return new ApiOk("Review deleted");
    }

    @PUT
    @AdminGuard
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiOk update(PublishedReview review,
                        @Context ContainerRequestContext ctx) {
        // Finds if the review with such id exists or not,
        // by calling the GET endpoint. If it does, then it
        // is updated.
        this.get(review.getId(), ctx);
        publishedReviewDAO.update(review);

        return new ApiOk("Review updated");
    }
}