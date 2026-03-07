package com.swe.EduMeter.orm.in_mem_dao;

import com.swe.EduMeter.models.Course;
import com.swe.EduMeter.models.Professor;
import com.swe.EduMeter.models.PublishedReview;
import com.swe.EduMeter.models.Teaching;
import com.swe.EduMeter.orm.dao.CourseDAO;
import com.swe.EduMeter.orm.dao.ProfDAO;
import com.swe.EduMeter.orm.dao.PublishedReviewDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InMemTeachingDAOTest {

    private InMemTeachingDAO teachingDAO;
    private Map<Integer, Teaching> store;

    @Mock
    private CourseDAO courseDAO;
    @Mock
    private ProfDAO profDAO;
    @Mock
    private PublishedReviewDAO reviewDAO;

    @BeforeEach
    public void setup() {
        store = new HashMap<>();
        // ID: 0, Course: 10, Prof: 100
        store.put(0, new Teaching(0, 10, 100));
        // ID: 1, Course: 10, Prof: 101
        store.put(1, new Teaching(1, 10, 101));
        // ID: 2, Course: 20, Prof: 100
        store.put(2, new Teaching(2, 20, 100));

        teachingDAO = new InMemTeachingDAO(store, courseDAO, profDAO, reviewDAO);
    }

    @Test
    public void testAdd_Success() {
        int courseId = 30;
        int profId = 102;
        Teaching newTeaching = new Teaching(null, courseId, profId);

        // Mock validations
        when(courseDAO.get(courseId)).thenReturn(Optional.of(new Course(courseId, "AI", 1)));
        when(profDAO.get(profId)).thenReturn(Optional.of(new Professor(profId, "Ada", "Lovelace")));

        int generatedId = teachingDAO.add(newTeaching);

        assertEquals(3, generatedId); // Incremental ID setup makes this 3
        assertTrue(store.containsKey(3));
    }

    @Test
    public void testAdd_InvalidProf_ThrowsException() {
        int invalidProfId = 999;
        Teaching newTeaching = new Teaching(null, 10, invalidProfId);

        when(profDAO.get(invalidProfId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> teachingDAO.add(newTeaching));
    }

    @Test
    public void testAdd_InvalidCourse_ThrowsException() {
        int invalidCourseId = 888;
        Teaching newTeaching = new Teaching(null, invalidCourseId, 100);

        // Prof is valid, but course is not
        when(profDAO.get(100)).thenReturn(Optional.of(new Professor(100, "X", "Y")));
        when(courseDAO.get(invalidCourseId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> teachingDAO.add(newTeaching));
    }

    @Test
    public void testDelete_CascadesToReviews() {
        int teachingId = 0;
        Teaching t = store.get(teachingId);

        // Mock the reviews linked to this specific Course/Prof combo
        PublishedReview r1 = new PublishedReview(); r1.setId(500);
        PublishedReview r2 = new PublishedReview(); r2.setId(501);

        when(reviewDAO.search(null, null, t.getCourseId(), t.getProfId(), null))
                .thenReturn(List.of(r1, r2));

        teachingDAO.delete(teachingId);

        assertFalse(store.containsKey(teachingId));
        // Verify reviews were deleted
        verify(reviewDAO).delete(500);
        verify(reviewDAO).delete(501);
    }

    @Test
    public void testDelete_NotFound_DoesNothing() {
        teachingDAO.delete(99);
        verifyNoInteractions(reviewDAO);
    }

    @Test
    public void testGetByCourse() {
        List<Teaching> results = teachingDAO.getByCourse(10);
        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(t -> t.getCourseId() == 10));
    }

    @Test
    public void testGetByProf() {
        List<Teaching> results = teachingDAO.getByProf(100);
        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(t -> t.getProfId() == 100));
    }

    @Test
    public void testGet_Found() {
        int validId = 0;

        Optional<Teaching> teaching = teachingDAO.get(validId);

        assertTrue(teaching.isPresent(), "Teaching should be present");
        assertEquals(validId, teaching.get().getId());
    }

    @Test
    public void testGet_NotFound() {
        int invalidId = store.size();

        Optional<Teaching> teaching = teachingDAO.get(invalidId);

        assertTrue(teaching.isEmpty(), "Teaching should not be present");
    }
}