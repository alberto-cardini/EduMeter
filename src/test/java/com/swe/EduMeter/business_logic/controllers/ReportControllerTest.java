package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.Report;
import com.swe.EduMeter.model.response.ApiObjectCreated;
import com.swe.EduMeter.model.response.ApiOk;
import com.swe.EduMeter.orm.PublishedReviewDAO;
import com.swe.EduMeter.orm.ReportDAO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportControllerTest {

    @Mock
    private ReportDAO reportDAO;

    @Mock
    private PublishedReviewDAO reviewDAO;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Principal principal;

    @InjectMocks
    private ReportController reportController;

    @Test
    public void testGetAll() {
        List<Report> reports = List.of(
                new Report(1, "Spam review", LocalDate.now(), "issuer1_hash", 101),
                new Report(2, "Inappropriate language", LocalDate.now(), "issuer2_hash", 102)
        );
        when(reportDAO.getAll()).thenReturn(reports);

        List<Report> gotReports = reportController.getAll();

        assertEquals(reports.size(), gotReports.size());
        verify(reportDAO, times(1)).getAll();
    }

    @Test
    public void testGet_Found() {
        int reportId = 42;
        Report report = new Report(reportId, "Spam review", LocalDate.now(), "issuer_hash", 101);
        when(reportDAO.get(reportId)).thenReturn(Optional.of(report));

        Report gotReport = reportController.get(reportId);

        assertEquals(report, gotReport);
    }

    @Test
    public void testGet_NotFound() {
        int reportId = 42;
        when(reportDAO.get(reportId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> reportController.get(reportId));

        assertEquals("Report not found", exception.getMessage());
    }

    @Test
    public void testCreate() {
        // Simulating the incoming request where ID and issuerHash are not yet set
        Report report = new Report(null, "Spam review", LocalDate.now(), null, 101);
        String expectedHash = "reporter_user_hash";
        int expectedId = 99;

        when(securityContext.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn(expectedHash);
        when(reportDAO.add(report)).thenReturn(expectedId);

        ApiObjectCreated response = reportController.create(securityContext, report);

        assertEquals(expectedId, response.id());
        assertEquals("Report created", response.message());

        verify(reportDAO, times(1)).add(report);
        assertEquals(expectedHash, report.getIssuerHash()); // Verifying the controller injected the hash correctly
    }

    @Test
    public void testDelete_Found() {
        int reportId = 42;
        Report report = new Report(reportId, "Spam review", LocalDate.now(), "issuer_hash", 101);
        when(reportDAO.get(reportId)).thenReturn(Optional.of(report));

        ApiOk response = reportController.delete(reportId);

        assertEquals("Report deleted", response.message());
        verify(reportDAO, times(1)).delete(reportId);
    }

    @Test
    public void testDelete_NotFound() {
        int reportId = 42;
        when(reportDAO.get(reportId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> reportController.delete(reportId));

        assertEquals("Report not found", exception.getMessage());
        verify(reportDAO, never()).delete(anyInt());
    }

    @Test
    public void testAcceptReport_Found() {
        int reportId = 42;
        int reviewId = 101;
        Report report = new Report(reportId, "Spam review", LocalDate.now(), "issuer_hash", reviewId);

        when(reportDAO.get(reportId)).thenReturn(Optional.of(report));

        ApiOk response = reportController.acceptReport(reportId);

        assertEquals("Report approved", response.message());

        // get() is called twice: once explicitly in acceptReport(), and once implicitly inside this.delete()
        verify(reportDAO, times(2)).get(reportId);

        // Verify both the report and the associated review are deleted
        verify(reportDAO, times(1)).delete(reportId);
        verify(reviewDAO, times(1)).delete(reviewId);
    }

    @Test
    public void testAcceptReport_NotFound() {
        int reportId = 42;
        when(reportDAO.get(reportId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> reportController.acceptReport(reportId));

        assertEquals("Report not found", exception.getMessage());

        // Ensure no cascading deletions happen if the report isn't found
        verify(reportDAO, never()).delete(anyInt());
        verify(reviewDAO, never()).delete(anyInt());
    }
}