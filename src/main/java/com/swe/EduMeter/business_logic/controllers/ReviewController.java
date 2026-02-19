package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.business_logic.auth.annotations.AdminGuard;
import com.swe.EduMeter.business_logic.auth.annotations.AuthGuard;
import com.swe.EduMeter.model.*;
import com.swe.EduMeter.model.response.ApiOk;
import com.swe.EduMeter.orm.ReviewDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.time.LocalDate;
import java.util.List;

@Path("/review")
public class ReviewController {

    private final ReviewDAO reviewDAO;

    @Inject
    public ReviewController(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    // --- ENDPOINT PUBBLICI / STUDENTE ---

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Review> search(@QueryParam("school_id") Integer school_id,
                               @QueryParam("degree_id") Integer degree_id,
                               @QueryParam("course_id") Integer course_id,
                               @QueryParam("professor_id") Integer prof_id) {
        // Restituisce solo le recensioni con stato ACCEPTED
        return reviewDAO.search(school_id, degree_id, course_id, prof_id);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Review getById(@PathParam("id") int id) {
        return reviewDAO.get(id)
                .orElseThrow(() -> new NotFoundException("Recensione non trovata."));
    }

    @POST
    @AuthGuard
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Review review, @Context SecurityContext sc) {
        review.setDate(LocalDate.now());
        review.setUp_vote(0);

        // Logica Auto-Accept: se non ci sono campi "raw" (varchar), è subito pubblicata
        if (review.isFullyStructured()) {
            review.setStatus(ReviewStatus.ACCEPTED);
        } else {
            review.setStatus(ReviewStatus.PENDING);
        }

        int id = reviewDAO.create(review);
        String msg = (review.getStatus() == ReviewStatus.ACCEPTED) ?
                "Pubblicata!" : "In attesa di moderazione.";

        return Response.status(Response.Status.CREATED)
                .entity(new ApiOk(msg)).build();
    }

    @POST
    @Path("/{id}/vote")
    @AuthGuard
    @Produces(MediaType.APPLICATION_JSON)
    public Response upvote(@PathParam("id") int id) {
        // Incrementa il contatore up_vote
        if (reviewDAO.incrementVote(id)) {
            return Response.ok(new ApiOk("Voto registrato.")).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // --- ENDPOINT DI GESTIONE (ADMIN) ---

    @GET
    @Path("/pending")
    @AdminGuard
    @Produces(MediaType.APPLICATION_JSON)
    public List<Review> getPending() {
        // Recupera tutte le recensioni con campi varchar da validare
        return reviewDAO.getAllPending();
    }

    @PUT
    @Path("/{id}/approve")
    @AdminGuard
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response approve(@PathParam("id") int id, Review validatedReview) {
        // L'admin può correggere i dati e mappare i "raw fields" su ID reali
        validatedReview.setId(id);
        validatedReview.setStatus(ReviewStatus.ACCEPTED);

        if (reviewDAO.update(validatedReview)) {
            return Response.ok(new ApiOk("Recensione approvata e pubblicata.")).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}/reject")
    @AdminGuard
    @Produces(MediaType.APPLICATION_JSON)
    public Response reject(@PathParam("id") int id) {
        // L'admin rifiuta la recensione (può essere eliminata o marcata REJECTED)
        if (reviewDAO.updateStatus(id, ReviewStatus.REJECTED)) {
            return Response.ok(new ApiOk("Recensione rifiutata.")).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    @AdminGuard
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) {
        if (reviewDAO.delete(id)) {
            return Response.ok(new ApiOk("Recensione eliminata.")).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}