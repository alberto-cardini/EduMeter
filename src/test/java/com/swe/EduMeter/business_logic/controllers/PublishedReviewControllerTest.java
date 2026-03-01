package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.business_logic.auth.filters.AuthFilter;
import com.swe.EduMeter.model.PublishedReview;
import com.swe.EduMeter.model.response.ApiObjectCreated;
import com.swe.EduMeter.model.response.ApiOk;
import com.swe.EduMeter.orm.PublishedReviewDAO;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.SecurityContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PublishedReviewControllerTest {

    @Mock
    private PublishedReviewDAO publishedReviewDAO;

    @Mock
    private ContainerRequestContext ctx;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Principal principal;

    @InjectMocks
    private PublishedReviewController publishedReviewController;

    @Test
    public void testSearch_Authenticated() {
        // Intercept 'new AuthFilter()' and make it do nothing (simulate success)
        try (MockedConstruction<AuthFilter> mocked = mockConstruction(AuthFilter.class,
                (mock, context) -> doNothing().when(mock).filter(any()))) {

            String expectedHash = "user_hash";
            when(ctx.getSecurityContext()).thenReturn(securityContext);
            when(securityContext.getUserPrincipal()).thenReturn(principal);
            when(principal.getName()).thenReturn(expectedHash);

            List<PublishedReview> reviews = List.of(
                    new PublishedReview(1, "creator", "Good", LocalDate.now(), 5, 3, 101, 10)
            );
            when(publishedReviewDAO.search(1, 2, 3, 4, expectedHash)).thenReturn(reviews);

            List<PublishedReview> gotReviews = publishedReviewController.search(1, 2, 3, 4, ctx);

            assertEquals(1, gotReviews.size());
            verify(publishedReviewDAO, times(1)).search(1, 2, 3, 4, expectedHash);
        }
    }

    @Test
    public void testSearch_Unauthenticated() {
        // Intercept 'new AuthFilter()' and make it throw an exception to trigger the catch block
        try (MockedConstruction<AuthFilter> mocked = mockConstruction(AuthFilter.class,
                (mock, context) -> doThrow(new RuntimeException("Not authenticated")).when(mock).filter(any()))) {

            List<PublishedReview> reviews = List.of(
                    new PublishedReview(1, "creator", "Good", LocalDate.now(), 5, 3, 101, 10)
            );
            // Verify it falls back to passing `null` for the userHash
            when(publishedReviewDAO.search(1, 2, 3, 4, null)).thenReturn(reviews);

            List<PublishedReview> gotReviews = publishedReviewController.search(1, 2, 3, 4, ctx);

            assertEquals(1, gotReviews.size());
            verify(publishedReviewDAO, times(1)).search(1, 2, 3, 4, null);
        }
    }

    @Test
    public void testGet_Authenticated_Found() {
        try (MockedConstruction<AuthFilter> mocked = mockConstruction(AuthFilter.class,
                (mock, context) -> doNothing().when(mock).filter(any()))) {

            String expectedHash = "user_hash";
            when(ctx.getSecurityContext()).thenReturn(securityContext);
            when(securityContext.getUserPrincipal()).thenReturn(principal);
            when(principal.getName()).thenReturn(expectedHash);

            int id = 42;
            PublishedReview review = new PublishedReview(id, "creator", "Good", LocalDate.now(), 5, 3, 101, 10);
            when(publishedReviewDAO.get(id, expectedHash)).thenReturn(Optional.of(review));

            PublishedReview gotReview = publishedReviewController.get(id, ctx);

            assertEquals(review, gotReview);
            verify(publishedReviewDAO, times(1)).get(id, expectedHash);
        }
    }

    @Test
    public void testGet_Unauthenticated_NotFound() {
        try (MockedConstruction<AuthFilter> mocked = mockConstruction(AuthFilter.class,
                (mock, context) -> doThrow(new RuntimeException("Not authenticated")).when(mock).filter(any()))) {

            int id = 42;
            when(publishedReviewDAO.get(id, null)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class,
                    () -> publishedReviewController.get(id, ctx));

            assertEquals("Review not found", exception.getMessage());
            verify(publishedReviewDAO, times(1)).get(id, null);
        }
    }

    @Test
    public void testPublish() {
        PublishedReview review = new PublishedReview(null, null, "Great course", null, 5, 2, 101, 0);
        String expectedHash = "new_creator_hash";
        int expectedId = 99;

        when(securityContext.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn(expectedHash);
        when(publishedReviewDAO.add(review)).thenReturn(expectedId);

        ApiObjectCreated response = publishedReviewController.publish(review, securityContext);

        assertEquals(expectedId, response.id());
        assertEquals("Published review", response.message());

        verify(publishedReviewDAO, times(1)).add(review);
        assertEquals(expectedHash, review.getCreatorHash());
        assertEquals(0, review.getUpvotes());
        assertNotNull(review.getDate());
    }

    @Test
    public void testToggleVote() {
        try (MockedConstruction<AuthFilter> mocked = mockConstruction(AuthFilter.class,
                (mock, context) -> doNothing().when(mock).filter(any()))) {

            String expectedHash = "voter_hash";
            when(ctx.getSecurityContext()).thenReturn(securityContext);
            when(securityContext.getUserPrincipal()).thenReturn(principal);
            when(principal.getName()).thenReturn(expectedHash);

            int id = 42;
            PublishedReview review = new PublishedReview(id, "creator", "Good", LocalDate.now(), 5, 3, 101, 10);

            // Mock the internal get() call
            when(publishedReviewDAO.get(id, expectedHash)).thenReturn(Optional.of(review));

            ApiOk response = publishedReviewController.toggleVote(id, ctx);

            assertEquals("Vote toggled", response.message());
            verify(publishedReviewDAO, times(1)).toggleUpvote(id, expectedHash);
        }
    }

    @Test
    public void testDelete() {
        try (MockedConstruction<AuthFilter> mocked = mockConstruction(AuthFilter.class,
                (mock, context) -> doNothing().when(mock).filter(any()))) {

            String expectedHash = "admin_hash";
            when(ctx.getSecurityContext()).thenReturn(securityContext);
            when(securityContext.getUserPrincipal()).thenReturn(principal);
            when(principal.getName()).thenReturn(expectedHash);

            int id = 42;
            PublishedReview review = new PublishedReview(id, "creator", "Good", LocalDate.now(), 5, 3, 101, 10);

            // Mock the internal get() call
            when(publishedReviewDAO.get(id, expectedHash)).thenReturn(Optional.of(review));

            ApiOk response = publishedReviewController.delete(id, ctx);

            assertEquals("Review deleted", response.message());
            verify(publishedReviewDAO, times(1)).delete(id);
        }
    }

    @Test
    public void testUpdate() {
        try (MockedConstruction<AuthFilter> mocked = mockConstruction(AuthFilter.class,
                (mock, context) -> doNothing().when(mock).filter(any()))) {

            String expectedHash = "admin_hash";
            when(ctx.getSecurityContext()).thenReturn(securityContext);
            when(securityContext.getUserPrincipal()).thenReturn(principal);
            when(principal.getName()).thenReturn(expectedHash);

            int id = 42;
            PublishedReview review = new PublishedReview(id, "creator", "Updated comment", LocalDate.now(), 4, 3, 101, 10);

            // Mock the internal get() call
            when(publishedReviewDAO.get(id, expectedHash)).thenReturn(Optional.of(review));

            ApiOk response = publishedReviewController.update(review, ctx);

            assertEquals("Review updated", response.message());
            verify(publishedReviewDAO, times(1)).update(review);
        }
    }
}