package com.swe.EduMeter.orm.in_mem_dao;

import com.swe.EduMeter.models.Report;
import com.swe.EduMeter.models.PublishedReview;
import com.swe.EduMeter.orm.dao.PublishedReviewDAO;
import com.swe.EduMeter.orm.dao.ReportDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InMemReportDAOTest {
    private ReportDAO reportDAO;
    private Map<Integer, Report> reportStore;

    @Mock
    private PublishedReviewDAO publishedReviewDAO;

    @BeforeEach
    public void setup() {
        reportStore = new HashMap<>(Map.of(
                0, new Report(0, "Inappropriate language", LocalDate.of(2023, 10, 1), "hash_A", 100),
                1, new Report(1, "Spam content", LocalDate.of(2023, 10, 5), "hash_B", 101),
                2, new Report(2, "Harassment", LocalDate.of(2023, 10, 10), "hash_C", 102)
        ));

        reportDAO = new InMemReportDAO(reportStore, publishedReviewDAO);
    }

    @Test
    public void TestGetReport_Found() {
        int expectedId = 0;

        Optional<Report> report = reportDAO.get(expectedId);

        assertTrue(report.isPresent(), "Report should be present");
        assertEquals(expectedId, report.get().getId());
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

        assertEquals(reportStore.size(), gotReports.size());
    }

    @Test
    public void TestAddReport_Success() {
        int targetReviewId = 103;
        Report newReport = new Report(null, "Fake review", LocalDate.now(), "hash_D", targetReviewId);

        // Mock the PublishedReviewDAO to simulate that the target review exists
        when(publishedReviewDAO.get(targetReviewId, null))
                .thenReturn(Optional.of(mock(PublishedReview.class)));

        int expectedId = reportStore.size();

        int gotId = reportDAO.add(newReport);
        Optional<Report> gotReport = reportDAO.get(gotId);

        assertEquals(expectedId, gotId);
        assertTrue(gotReport.isPresent(), "Report should be inserted");
        assertEquals(gotId, gotReport.get().getId());
        assertEquals("Fake review", gotReport.get().getComment());
    }

    @Test
    public void TestAddReport_ReviewNotFound() {
        int targetReviewId = 999;
        Report newReport = new Report(null, "Fake review", LocalDate.now(), "hash_E", targetReviewId);

        // Mock the PublishedReviewDAO to simulate that the target review does NOT exist
        when(publishedReviewDAO.get(targetReviewId, null)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reportDAO.add(newReport);
        });

        assertEquals("Invalid reportId", exception.getMessage());
    }

    @Test
    public void TestUpdateReport_Found() {
        Report report = reportStore.get(0);

        String updatedComment = "Updated comment TEST";
        report.setComment(updatedComment);

        reportDAO.update(report);

        Optional<Report> updatedReport = reportDAO.get(report.getId());

        assertTrue(updatedReport.isPresent(), "Report should be present");
        assertEquals(report.getId(), updatedReport.get().getId());
        assertEquals(updatedComment, updatedReport.get().getComment());
    }

    @Test
    public void TestUpdateReport_NotFound() {
        int invalidId = 999;
        Report report = new Report(invalidId, "Orphan report", LocalDate.now(), "hash_F", 104);

        reportDAO.update(report);

        Optional<Report> gotReport = reportDAO.get(invalidId);

        assertTrue(gotReport.isEmpty(), "Report should not be present because replace() ignores missing keys");
    }

    @Test
    public void TestDeleteReport_Found() {
        int idToDelete = 0;
        int prevSize = reportStore.size();

        reportDAO.delete(idToDelete);

        assertEquals(prevSize - 1, reportStore.size());
        assertNull(reportStore.get(idToDelete));
    }

    @Test
    public void TestDeleteReport_NotFound() {
        int invalidId = 999;
        int prevReportCount = reportStore.size();

        reportDAO.delete(invalidId);

        assertEquals(prevReportCount, reportStore.size());
    }
}