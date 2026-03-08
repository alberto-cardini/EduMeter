package com.swe.EduMeter.business_logic.endpoints;

import com.swe.EduMeter.models.DraftReview;
import com.swe.EduMeter.models.PublishedReview;
import com.swe.EduMeter.models.Teaching;
import com.swe.EduMeter.models.response.ApiObjectCreated;
import com.swe.EduMeter.models.response.ApiOk;
import com.swe.EduMeter.orm.dao.DraftReviewDAO;
import com.swe.EduMeter.orm.dao.PublishedReviewDAO;
import com.swe.EduMeter.orm.dao.TeachingDAO;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.SecurityContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DraftReviewEndpointsTest {

    @Mock
    private DraftReviewDAO reviewDAO;

    @Mock
    private TeachingDAO teachingDAO;

    @Mock
    private PublishedReviewDAO publishedReviewDAO;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Principal principal;

    @InjectMocks
    private DraftReviewEndpoints draftReviewEndpoints;

    @Test
    public void testGetAll() {
        List<DraftReview> reviews = List.of(
                new DraftReview(1, "user1_hash", "Great course", LocalDate.now(), 5, 2, "School of Engineering", "CS", "Algorithms", "Smith"),
                new DraftReview(2, "user2_hash", "Too hard", LocalDate.now(), 2, 5, "School of Engineering", "CS", "OS", "Doe")
        );
        when(reviewDAO.getAll()).thenReturn(reviews);

        List<DraftReview> gotReviews = draftReviewEndpoints.getAll();

        assertEquals(reviews.size(), gotReviews.size());
        verify(reviewDAO, times(1)).getAll();
    }

    @Test
    public void testGet_Found() {
        int id = 42;
        DraftReview review = new DraftReview(id, "user1_hash", "Great course", LocalDate.now(), 5, 2, "School of Engineering", "CS", "Algorithms", "Smith");
        when(reviewDAO.get(id)).thenReturn(Optional.of(review));

        DraftReview gotReview = draftReviewEndpoints.get(id);

        assertEquals(review, gotReview);
    }

    @Test
    public void testGet_NotFound() {
        int id = 42;
        when(reviewDAO.get(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> draftReviewEndpoints.get(id));

        assertEquals("Review not found", exception.getMessage());
    }

    @Test
    public void testPublish() {
        // Simulating an incoming POST request where ID, hash, and date are not yet set
        DraftReview review = new DraftReview(null, null, "Great course", null, 5, 2, "School of Engineering", "CS", "Algorithms", "Smith");

        String expectedHash = "user123_hash";
        int expectedId = 99;

        // Mock the SecurityContext to return our mocked Principal
        when(securityContext.getUserPrincipal()).thenReturn(principal);
        // Mock the Principal to return the expected hash/username
        when(principal.getName()).thenReturn(expectedHash);

        // Mock the DAO add method
        when(reviewDAO.add(review)).thenReturn(expectedId);

        ApiObjectCreated response = draftReviewEndpoints.insert(review, securityContext);

        // Verify the response
        assertEquals(expectedId, response.id());
        assertEquals("Published review", response.message());

        // Verify the controller mutated the review object correctly before saving
        verify(reviewDAO, times(1)).add(review);
        assertEquals(expectedHash, review.getCreatorHash());
        assertNotNull(review.getDate());
        assertEquals(LocalDate.now(), review.getDate());
    }

    @Test
    public void testDelete_Found() {
        int id = 42;
        DraftReview review = new DraftReview(id, "user1_hash", "Great course", LocalDate.now(), 5, 2, "School of Engineering", "CS", "Algorithms", "Smith");
        when(reviewDAO.get(id)).thenReturn(Optional.of(review));

        ApiOk response = draftReviewEndpoints.delete(id);

        assertEquals("Review deleted", response.message());
        verify(reviewDAO, times(1)).delete(id);
    }

    @Test
    public void testDelete_NotFound() {
        int id = 42;
        when(reviewDAO.get(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> draftReviewEndpoints.delete(id));

        assertEquals("Review not found", exception.getMessage());
        verify(reviewDAO, never()).delete(anyInt());
    }

    @Test
    public void testPublishDraftReview_Success() {
        int draftId = 42;
        int teachingId = 100;
        int newPublishedId = 999;

        DraftReviewEndpoints.PublishPayload payload = new DraftReviewEndpoints.PublishPayload(teachingId);
        DraftReview draftReview = new DraftReview(draftId, "user1_hash", "Great course", LocalDate.now(), 5, 2, "School", "CS", "Algo", "Smith");

        when(teachingDAO.get(teachingId)).thenReturn(Optional.of(mock(Teaching.class)));
        when(reviewDAO.get(draftId)).thenReturn(Optional.of(draftReview));
        when(publishedReviewDAO.add(any(PublishedReview.class))).thenReturn(newPublishedId);

        ApiObjectCreated response = draftReviewEndpoints.publish(draftId, payload);

        assertEquals(newPublishedId, response.id());
        assertEquals("Published review", response.message());

        verify(publishedReviewDAO, times(1)).add(any(PublishedReview.class));
        verify(reviewDAO, times(1)).delete(draftId);
    }

    @Test
    public void testPublishDraftReview_NullTeachingId_ThrowsBadRequest() {
        int draftId = 42;
        DraftReviewEndpoints.PublishPayload payload = new DraftReviewEndpoints.PublishPayload(null);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> draftReviewEndpoints.publish(draftId, payload));

        assertEquals("'teachingId' must be set", exception.getMessage());

        verify(publishedReviewDAO, never()).add(any());
        verify(reviewDAO, never()).delete(anyInt());
    }

    @Test
    public void testPublishDraftReview_TeachingNotFound_ThrowsNotFound() {
        int draftId = 42;
        int teachingId = 99;
        DraftReviewEndpoints.PublishPayload payload = new DraftReviewEndpoints.PublishPayload(teachingId);

        when(teachingDAO.get(teachingId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> draftReviewEndpoints.publish(draftId, payload));

        assertEquals("'teachingId' not found", exception.getMessage());

        verify(reviewDAO, never()).get(anyInt());
        verify(publishedReviewDAO, never()).add(any());
        verify(reviewDAO, never()).delete(anyInt());
    }

    @Test
    public void testPublishDraftReview_DraftNotFound_ThrowsNotFound() {
        int draftId = 42;
        int teachingId = 100;
        DraftReviewEndpoints.PublishPayload payload = new DraftReviewEndpoints.PublishPayload(teachingId);

        when(teachingDAO.get(teachingId)).thenReturn(Optional.of(mock(Teaching.class)));
        when(reviewDAO.get(draftId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> draftReviewEndpoints.publish(draftId, payload));

        assertEquals("Review not found", exception.getMessage());

        verify(publishedReviewDAO, never()).add(any());
        verify(reviewDAO, never()).delete(anyInt());
    }
}