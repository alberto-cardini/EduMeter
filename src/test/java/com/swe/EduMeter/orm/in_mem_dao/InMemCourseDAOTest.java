package com.swe.EduMeter.orm.in_mem_dao;

import com.swe.EduMeter.models.Course;
import com.swe.EduMeter.models.Degree;
import com.swe.EduMeter.models.Teaching;
import com.swe.EduMeter.orm.dao.DegreeDAO;
import com.swe.EduMeter.orm.dao.TeachingDAO;
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
public class InMemCourseDAOTest {
    private InMemCourseDAO courseDAO;
    private Map<Integer, Course> courseStore;

    @Mock
    private TeachingDAO teachingDAO;

    @Mock
    private DegreeDAO degreeDAO;

    @BeforeEach
    public void setup() {
        courseStore = new HashMap<>();
        courseStore.put(0, new Course(0, "Algorithms", 10));
        courseStore.put(1, new Course(1, "Data Structures", 10));
        courseStore.put(2, new Course(2, "Anatomy", 20));

        courseDAO = new InMemCourseDAO(courseStore, teachingDAO, degreeDAO);
    }

    @Test
    public void testGetCourse_Found() {
        Optional<Course> course = courseDAO.get(0);
        assertTrue(course.isPresent());
        assertEquals("Algorithms", course.get().getName());
    }

    @Test
    public void testAddCourse_Success() {
        int degreeId = 10;
        Course newCourse = new Course(null, "Operating Systems", degreeId);

        // Must mock degreeDAO.get because add() validates the degree exists
        when(degreeDAO.get(degreeId)).thenReturn(Optional.of(new Degree(degreeId, "CS", Degree.Type.Bachelor, 1)));

        int newId = courseDAO.add(newCourse);

        assertEquals(courseStore.size() - 1, newId);
        assertTrue(courseStore.containsKey(newId));
        assertEquals("Operating Systems", courseStore.get(newId).getName());
    }

    @Test
    public void testAddCourse_InvalidDegree_ThrowsException() {
        int invalidDegreeId = 99;
        Course newCourse = new Course(null, "Fake Course", invalidDegreeId);

        when(degreeDAO.get(invalidDegreeId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> courseDAO.add(newCourse));
    }

    @Test
    public void testDeleteCourse_CascadesToTeachings() {
        int courseId = 0;
        List<Teaching> mockTeachings = List.of(
                new Teaching(100, courseId, 50),
                new Teaching(101, courseId, 51)
        );

        when(teachingDAO.getByCourse(courseId)).thenReturn(mockTeachings);

        courseDAO.delete(courseId);

        assertFalse(courseStore.containsKey(courseId));
        verify(teachingDAO, times(1)).delete(100);
        verify(teachingDAO, times(1)).delete(101);
    }

    @Test
    public void testSearch_ByPattern() {
        List<Course> results = courseDAO.search("data", null, null);
        assertEquals(1, results.size());
        assertEquals("Data Structures", results.get(0).getName());
    }

    @Test
    public void testSearch_ByDegreeId() {
        List<Course> results = courseDAO.search(null, null, 10);
        assertEquals(2, results.size());
    }

    @Test
    public void testSearch_BySchoolId() {
        int schoolId = 1;
        // Mock the degrees so the search can resolve which school they belong to
        when(degreeDAO.get(10)).thenReturn(Optional.of(new Degree(10, "CS", Degree.Type.Bachelor, schoolId)));
        when(degreeDAO.get(20)).thenReturn(Optional.of(new Degree(20, "Med", Degree.Type.Master, 2)));

        List<Course> results = courseDAO.search(null, schoolId, null);

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(c -> c.getDegreeId() == 10));
    }

    @Test
    public void testSearch_BySchoolId_InvalidDegree_ThrowsException() {
        when(degreeDAO.get(anyInt())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> courseDAO.search(null, 1, null));
    }

    @Test
    public void testUpdateCourse() {
        Course course = courseStore.get(2);
        String newName = "Advanced Anatomy";
        course.setName(newName);

        courseDAO.update(course);

        assertEquals(newName, courseStore.get(2).getName());
    }
}