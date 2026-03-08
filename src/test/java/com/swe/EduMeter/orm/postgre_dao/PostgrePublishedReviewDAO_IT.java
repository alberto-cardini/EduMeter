package com.swe.EduMeter.orm.postgre_dao;

import com.swe.EduMeter.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PostgrePublishedReviewDAO_IT extends PostgreIT {
    private final PostgrePublishedReviewDAO reviewDAO = new PostgrePublishedReviewDAO();
    private final PostgreReportDAO reportDAO = new PostgreReportDAO();

    public static void insertPublishedReviews() throws SQLException {
        Connection c = DatabaseManager.getInstance().getConnection();
        String statement = "INSERT INTO Published_Review (user_id, date, teaching_id, enjoyment, difficulty, comment) VALUES" +
                "('user000000000000000001', '2024-03-01', 1, 8, 7, 'Great course, very challenging but rewarding.')," +
                "('user000000000000000002', '2024-03-05', 1, 6, 9, 'Difficult but the professor explains well.')," +
                "('user000000000000000001', '2024-03-10', 2, 7, 8, '')," +
                "('user000000000000000003', '2024-02-12', 3, 9, 6, 'Still drafting my thoughts on this one.');";

        try (PreparedStatement st = c.prepareStatement(statement)) {
            st.execute();
        }
    }

    public static void insertUpvotes() throws SQLException {
        Connection c = DatabaseManager.getInstance().getConnection();
        String statement = "INSERT INTO Up_Vote (review_id, user_id) VALUES" +
                "(1, 'user000000000000000001')," +
                "(1, 'user000000000000000002')," +
                "(2, 'user000000000000000001')," +
                "(2, 'user000000000000000003');";

        try (PreparedStatement st = c.prepareStatement(statement)) {
            st.execute();
        }

    }

    @BeforeEach
    public void setup() throws SQLException {
        PostgreSchoolDAO_IT.insertSchools();
        PostgreDegreeDAO_IT.insertDegrees();
        PostgreCourseDAO_IT.insertCourses();
        PostgreProfDAO_IT.insertProfessors();
        PostgreTeachingDAO_IT.insertTeachings();
        PostgreUserDAO_IT.insertUsers();
        insertPublishedReviews();
        insertUpvotes();
    }

    @Test
    public void testGet_Found() {
        int validId = 1;

        Optional<PublishedReview> review = reviewDAO.get(validId, "user000000000000000001");

        assertTrue(review.isPresent(), "Review should be present");
        assertEquals(2, review.get().getUpvotes());
        assertTrue(review.get().isUpvotedByUser());
    }

    @Test
    public void testGet_NotFound() {
        int invalidId = 999;

        Optional<PublishedReview> review = reviewDAO.get(invalidId, "user000000000000000001");

        assertTrue(review.isEmpty(), "Review should not be present");
    }

    @Test
    public void testAdd_ValidTeachingId() {
        int validTeachingId = 2;
        String comment = "c";
        PublishedReview newReview = new PublishedReview(null, "user000000000000000002", comment, LocalDate.now(), 3, 3, validTeachingId, 0);

        int generatedId = reviewDAO.add(newReview);
        Optional<PublishedReview> insertedReview = reviewDAO.get(generatedId, null);

        assertTrue(insertedReview.isPresent(), "Review should be inserted");
        assertEquals(comment, insertedReview.get().getComment());
    }

    @Test
    public void testAdd_InvalidTeachingId() {
        int invalidTeachingId = 999;
        PublishedReview newReview = new PublishedReview(null, "user000000000000000002", "c", LocalDate.now(), 3, 3, invalidTeachingId, 0);

        assertThrows(RuntimeException.class, () -> reviewDAO.add(newReview));
    }

    @Test
    public void testToggleUpvote_AddAndRemove() {
        int reviewId = 1;

        PublishedReview review = reviewDAO.get(reviewId, null)
                .orElseThrow(() -> new RuntimeException("Review should be present"));

        int prevCount = review.getUpvotes();

        // Toggle ON
        reviewDAO.toggleUpvote(reviewId, "user000000000000000003");
        review = reviewDAO.get(reviewId, null)
                .orElseThrow(() -> new RuntimeException("Review should be present"));
        int newCount = review.getUpvotes();

        assertEquals(prevCount + 1, newCount);

        // Toggle OFF
        reviewDAO.toggleUpvote(reviewId, "user000000000000000003");
        review = reviewDAO.get(reviewId, null)
                .orElseThrow(() -> new RuntimeException("Review should be present"));
        newCount = review.getUpvotes();

        assertEquals(prevCount, newCount);
    }

    @Test
    public void testDelete_CascadesToReports() throws SQLException {
        PostgreReportDAO_IT.insertReports();
        int validId = 1;

        reviewDAO.delete(validId);

        Optional<PublishedReview> deletedReview = reviewDAO.get(validId, null);
        List<Report> reports = reportDAO.getAll();

        assertTrue(deletedReview.isEmpty(), "Review should be deleted");
        assertFalse(reports.stream().anyMatch(r -> r.getReviewId().equals(validId)), "Reports should be cascade deleted");
    }

    @Test
    public void testSearch_All() {
        List<PublishedReview> results = reviewDAO.search(null, null, null, null, null);

        assertEquals(4, results.size());
    }

    @Test
    public void testSearch_FiltersByProfessorAndCourse() {
        int courseId = 1;
        int profId = 1;
        List<PublishedReview> results = reviewDAO.search(null, null, courseId, profId, null);

        assertEquals(2, results.size());
    }

    @Test
    public void testSearch_DeepFilter_SchoolAndDegree() {
        int schoolId = 1;
        int degreeId = 1;
        int courseId = 1;
        int profId = 1;

        List<PublishedReview> results = reviewDAO.search(schoolId, degreeId, courseId, profId, null);

        assertEquals(2, results.size());
    }

    @Test
    public void testUpdate_Found() {
        int validId = 1;
        PublishedReview review = reviewDAO.get(validId, null)
                .orElseThrow(() -> new RuntimeException("Review should be present"));

        String newComment = "NEW COMMENT";
        int newEnjoyment = 0;
        int newDifficulty = 0;
        review.setComment(newComment);
        review.setEnjoyment(newEnjoyment);
        review.setDifficulty(newDifficulty);

        reviewDAO.update(review);
        Optional<PublishedReview> updatedReview = reviewDAO.get(validId, null);

        assertTrue(updatedReview.isPresent(), "Review should be present");
        assertEquals(newComment, updatedReview.get().getComment());
        assertEquals(newEnjoyment, updatedReview.get().getEnjoyment());
        assertEquals(newDifficulty, updatedReview.get().getDifficulty());
    }

    @Test
    public void testUpdate_NotFound() {
        int invalidId = 999;
        PublishedReview invalidReview = new PublishedReview(invalidId, "user000000000000000001",
                "Great course, very challenging but rewarding.", LocalDate.now(),
                1, 8, 7, 0
        );
        invalidReview.setId(invalidId);

        reviewDAO.update(invalidReview);
        Optional<PublishedReview> gotReview = reviewDAO.get(invalidId, null);

        assertTrue(gotReview.isEmpty(), "Review should not be present");
    }
}
