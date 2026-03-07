package com.swe.EduMeter.orm.in_mem_dao;

import com.swe.EduMeter.models.*;
import com.swe.EduMeter.orm.dao.CourseDAO;
import com.swe.EduMeter.orm.dao.DegreeDAO;
import com.swe.EduMeter.orm.dao.ReportDAO;
import com.swe.EduMeter.orm.dao.TeachingDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InMemPublishedReviewDAOTest {
    private InMemPublishedReviewDAO reviewDAO;
    private Map<Integer, PublishedReview> reviewStore;
    private Map<Integer, Set<String>> upvoteStore;

    @Mock private TeachingDAO teachingDAO;
    @Mock private CourseDAO courseDAO;
    @Mock private DegreeDAO degreeDAO;
    @Mock private ReportDAO reportDAO;

    private final String USER_HASH = "user_123";

    @BeforeEach
    public void setup() {
        reviewStore = new HashMap<>();
        upvoteStore = new HashMap<>();

        // Setup 2 reviews
        PublishedReview r1 = new PublishedReview(0, "hash1", "Good", LocalDate.now(), 5, 2, 100, 0);
        PublishedReview r2 = new PublishedReview(1, "hash2", "Bad", LocalDate.now(), 1, 5, 101, 0);

        reviewStore.put(0, r1);
        reviewStore.put(1, r2);

        // Setup upvotes (Review 0 has one upvote)
        Set<String> upvotes0 = ConcurrentHashMap.newKeySet();
        upvotes0.add(USER_HASH);
        upvoteStore.put(0, upvotes0);
        upvoteStore.put(1, ConcurrentHashMap.newKeySet());

        reviewDAO = new InMemPublishedReviewDAO(reviewStore, upvoteStore, teachingDAO, courseDAO, degreeDAO, reportDAO);
    }

    @Test
    public void testGet() {
        Optional<PublishedReview> result = reviewDAO.get(0, USER_HASH);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getUpvotes());
        assertTrue(result.get().isUpvotedByUser());
    }

    @Test
    public void testAdd_ValidatesTeachingAndInitializesUpvotes() {
        PublishedReview newReview = new PublishedReview(null, "h", "c", LocalDate.now(), 3, 3, 200, 0);

        when(teachingDAO.get(200)).thenReturn(Optional.of(new Teaching(200, 10, 50)));

        int newId = reviewDAO.add(newReview);

        assertEquals(2, newId);
        assertTrue(reviewStore.containsKey(2));
        assertNotNull(upvoteStore.get(2), "Upvote set should be initialized");
    }

    @Test
    public void testToggleUpvote_AddAndRemove() {
        String newUser = "new_user";

        // Toggle ON
        reviewDAO.toggleUpvote(1, newUser);
        assertTrue(upvoteStore.get(1).contains(newUser));

        // Toggle OFF
        reviewDAO.toggleUpvote(1, newUser);
        assertFalse(upvoteStore.get(1).contains(newUser));
    }

    @Test
    public void testDelete_CascadesToReports() {
        int reviewIdToDelete = 0;

        // Mock existing reports
        Report rep1 = new Report(10, "Spam", LocalDate.now(), "hash", reviewIdToDelete);
        Report rep2 = new Report(11, "Other", LocalDate.now(), "hash", 99);

        when(reportDAO.getAll()).thenReturn(List.of(rep1, rep2));

        reviewDAO.delete(reviewIdToDelete);

        assertFalse(reviewStore.containsKey(reviewIdToDelete));
        assertFalse(upvoteStore.containsKey(reviewIdToDelete));

        verify(reportDAO, times(1)).delete(10);
        verify(reportDAO, never()).delete(11);
    }

    @Test
    public void testSearch_All() {
        List<PublishedReview> results = reviewDAO.search(null, null, null, null, null);

        assertEquals(reviewStore.size(), results.size());
    }

    @Test
    public void testSearch_FiltersByProfessorAndCourse() {
        int profId = 50;
        int courseId = 10;

        // Review 0 belongs to Teaching 100
        when(teachingDAO.get(100)).thenReturn(Optional.of(new Teaching(100, courseId, profId)));
        // Review 1 belongs to Teaching 101
        when(teachingDAO.get(101)).thenReturn(Optional.of(new Teaching(101, 99, 99)));

        List<PublishedReview> results = reviewDAO.search(null, null, courseId, profId, null);

        assertEquals(1, results.size());
        assertEquals(0, results.get(0).getId());
    }

    @Test
    public void testSearch_DeepFilter_SchoolAndDegree() {
        int schoolId = 1;
        int degreeId = 5;
        int courseId = 10;
        int profId = 50;

        // Mock the chain: Review -> Teaching -> Course -> Degree -> School
        when(teachingDAO.get(100)).thenReturn(Optional.of(new Teaching(100, courseId, profId)));
        when(teachingDAO.get(101)).thenReturn(Optional.of(new Teaching(101, 42, 94)));
        when(courseDAO.get(courseId)).thenReturn(Optional.of(new Course(courseId, "Algorithms", degreeId)));
        when(degreeDAO.get(degreeId)).thenReturn(Optional.of(new Degree(degreeId, "CS", Degree.Type.Bachelor, schoolId)));

       List<PublishedReview> results = reviewDAO.search(schoolId, degreeId, courseId, profId, null);

       assertEquals(1, results.size());
        assertEquals(0, results.get(0).getId());
    }

    @Test
    public void testSearch_InvalidTeaching_ThrowsException() {
        // Review 0 has teachingId 100, we'll make it "disappear" from the DAO
        when(teachingDAO.get(100)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                reviewDAO.search(null, null, 10, 50, null)
        );
    }

    @Test
    public void testUpdate_Found() {
        int validId = 0;
        PublishedReview review = reviewStore.get(validId);
        String newComment = "NEW COMMENT";
        int newEnjoyment = 0;
        int newDifficulty = 0;
        review.setComment(newComment);
        review.setEnjoyment(newEnjoyment);
        review.setDifficulty(newDifficulty);

        reviewDAO.update(review);

        PublishedReview updatedReview = reviewStore.get(validId);
        assertEquals(newComment, updatedReview.getComment());
        assertEquals(newEnjoyment, updatedReview.getEnjoyment());
        assertEquals(newDifficulty, updatedReview.getDifficulty());
    }

    @Test
    public void testUpdate_NotFound() {
        int invalidId = reviewStore.size();
        int prevSize = reviewStore.size();
        PublishedReview invalidReview = new PublishedReview();
        invalidReview.setId(invalidId);

        reviewDAO.update(invalidReview);

        assertEquals(prevSize, reviewStore.size());
        assertFalse(reviewStore.containsKey(invalidId));
    }
}