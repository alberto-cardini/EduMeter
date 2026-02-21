package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.*;
import com.swe.EduMeter.orm.PublishedReviewDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.time.LocalDate;
import java.util.List;

@Path("/review")
public class ReviewController {

    private final PublishedReviewDAO reviewDAO;

    @Inject
    public ReviewController(PublishedReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Review> search(@QueryParam("school_id") Integer schoolId,
                               @QueryParam("degree_id") Integer degreeId,
                               @QueryParam("course_id") Integer courseId,
                               @QueryParam("professor_id") Integer profId){
        return reviewDAO.search(schoolId, degreeId, courseId, profId);
    }

    @GET
    @Path("/{review_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Review getById(@PathParam("review_id") int id)
    {
        return reviewDAO.get(id, ReviewStatus.ACCEPTED).orElseThrow(() -> new NotFoundException("Review not found"));
    }

    @POST
    //@AuthGuard
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Integer create(Review review, @Context SecurityContext sc)
    {
        review.setCreatorHash(sc.getUserPrincipal().getName());
        review.setDate(LocalDate.now());
        review.setUp_vote(0);

        // Logica Auto-Accept: se non ci sono campi "raw" (varchar), è subito pubblicata
        if (review.isFullyStructured()) {
            review.setStatus(ReviewStatus.ACCEPTED);
        } else {
            review.setStatus(ReviewStatus.PENDING);
        }

        return reviewDAO.add(review);
    }

    @POST
    @Path("/{review_id}/vote")
    //@AuthGuard
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean upvote(@PathParam("review_id") int id)
    {
        return reviewDAO.get(id, ReviewStatus.ACCEPTED)
                .map(review -> reviewDAO.incrementVote(id))
                .orElseThrow(() -> new NotFoundException("Review not found"));
    }

    @GET
    @Path("/pending")
    //@AdminGuard
    @Produces(MediaType.APPLICATION_JSON)
    public List<Review> getPending()
    {
        return reviewDAO.list(ReviewStatus.PENDING);
    }

    @PUT
    @Path("/{review_id}/approve")
    //@AdminGuard
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean approve(@PathParam("review_id") int id, Review validatedReview)
    {
        validatedReview.setId(id);
        validatedReview.setStatus(ReviewStatus.ACCEPTED);
        return reviewDAO.update(validatedReview);
    }

    @PUT
    @Path("/{review_id}/reject")
    //@AdminGuard
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean reject(@PathParam("review_id") int id)
    {
        return reviewDAO.delete(id);
    }

    @DELETE
    @Path("/{review_id}")
    //@AdminGuard
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean delete(@PathParam("review_id") int id)
    {
        return reviewDAO.delete(id);
    }
}