package com.swe.EduMeter.orm.postgre_dao;

import com.swe.EduMeter.models.DraftReview;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PostgreDraftReviewDAO_IT extends PostgreIT {
    private final PostgreDraftReviewDAO reviewDAO = new PostgreDraftReviewDAO();

    public static void insertDraftReviews() throws SQLException {
        Connection c = DatabaseManager.getInstance().getConnection();
        String statement = "INSERT INTO Drafted_Review (user_id, date, school, degree, course, professor, enjoyment, difficulty, comment) VALUES" +
                "('user000000000000000001', '2024-03-01', 'Greenwood High', 'Computer Science', 'Algorithms 101', 'Prof. Johnson', 8, 7, 'Great course, very challenging but rewarding.')," +
                "('user000000000000000002', '2024-03-05', 'St. Mary''s Academy', 'Mathematics', 'Calculus II', 'Prof. Smith', 6, 9, 'Difficult but the professor explains well.')," +
                "('user000000000000000001', '2024-03-10', 'Riverside Technical School', 'Engineering', 'Thermodynamics', 'Prof. Garcia', 7, 8, '')," +
                "('user000000000000000003', '2024-02-12', 'Greenwood High', 'Computer Science', 'Data Structures', 'Prof. Lee', 9, 6, 'Still drafting my thoughts on this one.');";

        try (PreparedStatement st = c.prepareStatement(statement)) {
            st.execute();
        }
    }

    @BeforeEach
    public void setup() throws SQLException {
        PostgreUserDAO_IT.insertUsers();
        insertDraftReviews();
    }

    @Test
    public void testUpdate_Found() {
        int reviewId = 1;
        String newComment = "Actually, it was harder";
        int newEnjoyment = 3;
        int newDifficulty = 5;
        String newCourse = "Advanced Algorithms";

        DraftReview updated = reviewDAO.get(reviewId)
                .orElseThrow(() -> new RuntimeException("Review should be present"));
        updated.setComment(newComment);
        updated.setEnjoyment(newEnjoyment);
        updated.setDifficulty(newDifficulty);
        updated.setRawCourse(newCourse);

        reviewDAO.update(updated);

        Optional<DraftReview> updatedReview = reviewDAO.get(reviewId);

        assertTrue(updatedReview.isPresent(), "Review should be present");
        assertEquals(newComment, updatedReview.get().getComment());
        assertEquals(newEnjoyment, updatedReview.get().getEnjoyment());
        assertEquals(newDifficulty, updatedReview.get().getDifficulty());
        assertEquals(newCourse, updatedReview.get().getRawCourse());
    }

    @Test
    public void testUpdate_NotFound() {
        int invalidId = 999;
        DraftReview ghostReview = new DraftReview(
                invalidId, "h", "c", LocalDate.now(), 1, 1, "S", "D", "C", "P"
        );

        reviewDAO.update(ghostReview);

        Optional<DraftReview> nonExistingReview = reviewDAO.get(invalidId);

        assertTrue(nonExistingReview.isEmpty(), "Review should not be present");
    }

    @Test
    public void testAdd() {
        String comment = "New Draft";
        DraftReview newDraft = new DraftReview(
                null, "user000000000000000002", comment, LocalDate.now(),
                5, 2, "School X", "Degree Y", "Course Z", "Prof W"
        );
        int generatedId = reviewDAO.add(newDraft);
        Optional<DraftReview> insertedReview = reviewDAO.get(generatedId);

        assertTrue(insertedReview.isPresent(), "Review should be inserted");
        assertEquals(comment, insertedReview.get().getComment());
    }

    @Test
    public void testDelete_Found() {
        int validId = 1;
        reviewDAO.delete(validId);

        Optional<DraftReview> gotReview = reviewDAO.get(validId);

        assertTrue(gotReview.isEmpty(), "Review should be deleted");
    }

    @Test
    public void testDelete_NotFound() {
        int invalidId = 999;

        assertDoesNotThrow(() -> reviewDAO.delete(invalidId));
    }

    @Test
    public void testGet_Found() {
        int validId = 1;

        Optional<DraftReview> result = reviewDAO.get(validId);

        assertTrue(result.isPresent());
        assertEquals("Prof. Johnson", result.get().getRawProfessor());
    }

    @Test
    public void testGet_NotFound() {
        int invalidId = 999;

        Optional<DraftReview> result = reviewDAO.get(invalidId);
        assertTrue(result.isEmpty(), "Review should not be present");
    }

    @Test
    public void testGet_All() {
        List<DraftReview> reviews = reviewDAO.getAll();

        assertEquals(4, reviews.size());
    }

}
