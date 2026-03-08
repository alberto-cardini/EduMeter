package com.swe.EduMeter.business_logic.endpoints;

import com.swe.EduMeter.business_logic.auth.annotations.AdminGuard;
import com.swe.EduMeter.business_logic.auth.annotations.AuthGuard;
import com.swe.EduMeter.models.DraftReview;
import com.swe.EduMeter.models.PublishedReview;
import com.swe.EduMeter.models.response.ApiObjectCreated;
import com.swe.EduMeter.models.response.ApiOk;
import com.swe.EduMeter.orm.dao.DraftReviewDAO;
import com.swe.EduMeter.orm.dao.PublishedReviewDAO;
import com.swe.EduMeter.orm.dao.TeachingDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

import java.time.LocalDate;
import java.util.List;

@Path("/review/draft")
public class DraftReviewEndpoints {
    private final DraftReviewDAO draftReviewDAO;
    private final PublishedReviewDAO publishedReviewDAO;
    private final TeachingDAO teachingDAO;

    @Inject
    public DraftReviewEndpoints(DraftReviewDAO draftReviewDAO, PublishedReviewDAO publishedReviewDAO,
                                TeachingDAO teachingDAO) {
        this.draftReviewDAO = draftReviewDAO;
        this.publishedReviewDAO = publishedReviewDAO;
        this.teachingDAO = teachingDAO;
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public List<DraftReview> getAll() {
        return draftReviewDAO.getAll();
    }

    @GET
    @Path("/{review_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @AdminGuard
    public DraftReview get(@PathParam("review_id") int id) {
        return draftReviewDAO.get(id).orElseThrow(() -> new NotFoundException("Review not found"));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AuthGuard
    public ApiObjectCreated insert(DraftReview review, @Context SecurityContext sc) {
        review.setCreatorHash(sc.getUserPrincipal().getName());
        review.setDate(LocalDate.now());

        int reviewId = draftReviewDAO.add(review);

        return new ApiObjectCreated(reviewId, "Published review");
    }

    @DELETE
    @Path("/{review_id}")
    @AdminGuard
    public ApiOk delete(@PathParam("review_id") int id) {
        this.get(id);

        draftReviewDAO.delete(id);

        return new ApiOk("Review deleted");
    }

    @POST
    @Path("/{review_id}/publish")
    @AdminGuard
    public ApiObjectCreated publish(@PathParam("review_id") int draftReviewId, PublishPayload payload) {
        if (payload.teachingId() == null) {
            throw new BadRequestException("'teachingId' must be set");
        }

        teachingDAO.get(payload.teachingId())
                   .orElseThrow(() -> new NotFoundException("'teachingId' not found"));

        DraftReview draftReview = this.get(draftReviewId);

        PublishedReview publishedReview = new PublishedReview(
                null, draftReview.getCreatorHash(), draftReview.getComment(),
                draftReview.getDate(), draftReview.getEnjoyment(), draftReview.getDifficulty(),
                payload.teachingId(), 0
        );

        int publishedReviewId = publishedReviewDAO.add(publishedReview);
        draftReviewDAO.delete(draftReviewId);

        return new ApiObjectCreated(publishedReviewId, "Published review");
    }

    record PublishPayload(Integer teachingId) {}
}
