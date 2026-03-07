package com.swe.EduMeter.orm.in_mem_dao;

import com.swe.EduMeter.models.Course;
import com.swe.EduMeter.models.Degree;
import com.swe.EduMeter.models.School;
import com.swe.EduMeter.orm.dao.CourseDAO;
import com.swe.EduMeter.orm.dao.SchoolDAO;
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
public class InMemDegreeDAOTest {
    private InMemDegreeDAO degreeDAO;
    private Map<Integer, Degree> degreeStore;

    @Mock
    private CourseDAO courseDAO;

    @Mock
    private SchoolDAO schoolDAO;

    @BeforeEach
    public void setup() {
        degreeStore = new HashMap<>(Map.of(
                0, new Degree(0, "Computer Engineering", Degree.Type.Bachelor, 0),
                1, new Degree(1, "Law", Degree.Type.Master, 1),
                2, new Degree(2, "Medicine", Degree.Type.Master, 2)
        ));
        degreeDAO = new InMemDegreeDAO(degreeStore, courseDAO, schoolDAO);
    }

    @Test
    public void TestGetDegree_Found() {
        int expectedId = 1;
        Optional<Degree> degree = degreeDAO.get(expectedId);

        assertTrue(degree.isPresent());
        assertEquals(expectedId, degree.get().getId());
    }

    @Test
    public void TestGetDegree_NotFound() {
        int invalidId = degreeStore.size();
        Optional<Degree> degree = degreeDAO.get(invalidId);

        assertTrue(degree.isEmpty());
    }

    @Test
    public void TestSearchDegree_ByPatternAndSchool() {
        // Search for "Law" in School 1
        List<Degree> results = degreeDAO.search("law", 1);
        assertEquals(1, results.size());
        assertEquals("Law", results.get(0).getName());

        // Search for "Law" in School 2
        List<Degree> emptyResults = degreeDAO.search("law", 2);
        assertTrue(emptyResults.isEmpty());
    }

    @Test
    public void TestAddDegree_Success() {
        int schoolId = 0;
        String courseName = "Space Engineering";
        Degree newDegree = new Degree(null, courseName, Degree.Type.Master, schoolId);

        when(schoolDAO.get(schoolId)).thenReturn(Optional.of(new School(schoolId, "Engineering")));

        int generatedId = degreeDAO.add(newDegree);

        assertEquals(degreeStore.size() - 1, generatedId);
        assertTrue(degreeStore.containsKey(generatedId));
        assertEquals(courseName, degreeStore.get(generatedId).getName());
    }

    @Test
    public void TestAddDegree_InvalidSchool_ThrowsException() {
        int invalidSchoolId = 500;
        Degree newDegree = new Degree(null, "Ghost Degree", Degree.Type.Bachelor, invalidSchoolId);

        when(schoolDAO.get(invalidSchoolId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> degreeDAO.add(newDegree));
    }

    @Test
    public void TestUpdateDegree() {
        int validId = 0;
        Degree degree = degreeStore.get(validId);
        String newName = "Updated Name";
        degree.setName(newName);

        degreeDAO.update(degree);

        assertEquals(newName, degreeStore.get(validId).getName());
    }

    @Test
    public void TestDeleteDegree_CascadesToCourses() {
        int degreeIdToDelete = 0;
        List<Course> mockCourses = List.of(
                new Course(10, "Math", degreeIdToDelete),
                new Course(11, "Physics", degreeIdToDelete)
        );

        // When searching for courses linked to this degree
        when(courseDAO.search(null, null, degreeIdToDelete)).thenReturn(mockCourses);

        degreeDAO.delete(degreeIdToDelete);

        assertFalse(degreeStore.containsKey(degreeIdToDelete));

        // Verify courseDAO.delete was called for each child course
        verify(courseDAO, times(1)).delete(10);
        verify(courseDAO, times(1)).delete(11);
    }
}