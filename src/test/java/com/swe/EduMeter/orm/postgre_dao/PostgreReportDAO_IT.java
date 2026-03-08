package com.swe.EduMeter.orm.postgre_dao;

import com.swe.EduMeter.models.PublishedReview;
import com.swe.EduMeter.models.Report;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostgreReportDAO_IT extends PostgreIT {
    private final PostgreReportDAO reportDAO = new PostgreReportDAO();

    public static void insertReports() throws SQLException {
        Connection c = DatabaseManager.getInstance().getConnection();
        // 5 prof, 5 corsi
        String statement = "INSERT INTO Report (comment, date, user_id, review_id) VALUES" +
                "('This review contains inappropriate language.', '2024-03-01', 'user000000000000000001', 1)," +
                "('Spam content, same review posted multiple times.', '2024-03-05', 'user000000000000000002', 2)," +
                "(null, '2024-03-10', 'user000000000000000003', 3)," +
                "('Review seems fake, unlikely experience.', '2024-03-12', 'user000000000000000001', 4);";

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
        PostgrePublishedReviewDAO_IT.insertPublishedReviews();
        insertReports();
    }

    @Test
    public void TestGetReport_Found() {
        int expectedId = 1;

        Optional<Report> report = reportDAO.get(expectedId);

        assertTrue(report.isPresent(), "Report should be present");
        assertEquals("This review contains inappropriate language.", report.get().getComment());
    }

    @Test
    public void TestGetReport_NotFound() {
        int invalidId = 999;

        Optional<Report> report = reportDAO.get(invalidId);

        assertTrue(report.isEmpty(), "Report should not be present");
    }

    @Test
    public void TestGetAllReports() {
        List<Report> gotReports = reportDAO.getAll();

        assertEquals(4, gotReports.size());
    }

    @Test
    public void TestAddReport_Success() {
        int targetReviewId = 1;
        String comment = "Fake review";
        Report newReport = new Report(null, comment, LocalDate.now(), "user000000000000000003", targetReviewId);

        int generatedId = reportDAO.add(newReport);
        Optional<Report> insertedReport = reportDAO.get(generatedId);

        assertTrue(insertedReport.isPresent(), "Report should be inserted");
        assertEquals(comment, insertedReport.get().getComment());
    }

    @Test
    public void TestAddReport_InvalidReview() {
        int targetReviewId = 999;
        Report newReport = new Report(null, "Fake review", LocalDate.now(), "user000000000000000003", targetReviewId);

        assertThrows(RuntimeException.class, () -> reportDAO.add(newReport));
    }

    @Test
    public void TestUpdateReport_Found() {
        int validId = 1;
        Report report = reportDAO.get(validId)
                .orElseThrow(() -> new RuntimeException("Report should be present"));

        String updatedComment = "Updated comment TEST";
        report.setComment(updatedComment);

        reportDAO.update(report);
        Optional<Report> updatedReport = reportDAO.get(report.getId());

        assertTrue(updatedReport.isPresent(), "Report should be present");
        assertEquals(updatedComment, updatedReport.get().getComment());
    }

    @Test
    public void TestUpdateReport_NotFound() {
        int invalidId = 999;
        Report report = new Report(invalidId, "Orphan report", LocalDate.now(), "user000000000000000003", 1);

        reportDAO.update(report);
        Optional<Report> gotReport = reportDAO.get(invalidId);

        assertTrue(gotReport.isEmpty(), "Report should not be present because replace() ignores missing keys");
    }

    @Test
    public void TestDeleteReport_Found() {
        int validId = 1;

        reportDAO.delete(validId);
        Optional<Report> deletedReport = reportDAO.get(validId);

        assertTrue(deletedReport.isEmpty(), "Report should be deleted");
    }

    @Test
    public void TestDeleteReport_NotFound() {
        int invalidId = 999;

        reportDAO.delete(invalidId);
        Optional<Report> invalidReport = reportDAO.get(invalidId);

        assertTrue(invalidReport.isEmpty(), "Report should not be present");
    }
}
