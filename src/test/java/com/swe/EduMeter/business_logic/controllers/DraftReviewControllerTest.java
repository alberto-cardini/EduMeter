package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.DraftReview;
import com.swe.EduMeter.model.response.ApiObjectCreated;
import com.swe.EduMeter.model.response.ApiOk;
import com.swe.EduMeter.orm.DraftReviewDAO;
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
public class DraftReviewControllerTest {

    @Mock
    private DraftReviewDAO reviewDAO;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Principal principal;

    @InjectMocks
    private DraftReviewController draftReviewController;

    @Test
    public void testGetAll() {
        List<DraftReview> reviews = List.of(
                new DraftReview(1, "user1_hash", "Great course", LocalDate.now(), 5, 2, "School of Engineering", "CS", "Algorithms", "Smith"),
                new DraftReview(2, "user2_hash", "Too hard", LocalDate.now(), 2, 5, "School of Engineering", "CS", "OS", "Doe")
        );
        when(reviewDAO.getAll()).thenReturn(reviews);

        List<DraftReview> gotReviews = draftReviewController.getAll();

        assertEquals(reviews.size(), gotReviews.size());
        verify(reviewDAO, times(1)).getAll();
    }

    @Test
    public void testGet_Found() {
        int id = 42;
        DraftReview review = new DraftReview(id, "user1_hash", "Great course", LocalDate.now(), 5, 2, "School of Engineering", "CS", "Algorithms", "Smith");
        when(reviewDAO.get(id)).thenReturn(Optional.of(review));

        DraftReview gotReview = draftReviewController.get(id);

        assertEquals(review, gotReview);
    }

    @Test
    public void testGet_NotFound() {
        int id = 42;
        when(reviewDAO.get(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> draftReviewController.get(id));

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

        ApiObjectCreated response = draftReviewController.publish(review, securityContext);

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

        ApiOk response = draftReviewController.delete(id);

        assertEquals("Review deleted", response.message());
        verify(reviewDAO, times(1)).delete(id);
    }

    @Test
    public void testDelete_NotFound() {
        int id = 42;
        when(reviewDAO.get(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> draftReviewController.delete(id));

        assertEquals("Review not found", exception.getMessage());
        verify(reviewDAO, never()).delete(anyInt());
    }
}