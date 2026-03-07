package com.swe.EduMeter.orm.in_mem_dao;

import com.swe.EduMeter.models.Professor;
import com.swe.EduMeter.models.Teaching;
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
public class InMemProfDAOTest {
    private InMemProfDAO profDAO;
    private Map<Integer, Professor> profStore;

    @Mock
    private TeachingDAO teachingDAO;

    @BeforeEach
    public void setup() {
        profStore = new HashMap<>();
        profStore.put(0, new Professor(0, "Alberto", "Cardini"));
        profStore.put(1, new Professor(1, "Lorenzo", "Bellina"));
        profStore.put(2, new Professor(2, "Marco", "Bertini"));

        profDAO = new InMemProfDAO(profStore, teachingDAO);
    }

    @Test
    public void testAddProfessor() {
        Professor prof = new Professor(null, "Fabio", "Corradi");
        int newId = profDAO.add(prof);

        assertEquals(profStore.size() - 1, newId);
        assertTrue(profStore.containsKey(newId));
        assertEquals("Fabio", profStore.get(newId).getName());
    }

    @Test
    public void testDeleteProfessor_CascadesToTeachings() {
        int profId = 0;
        List<Teaching> mockTeachings = List.of(
                new Teaching(100, 10, profId),
                new Teaching(101, 11, profId)
        );

        when(teachingDAO.getByProf(profId)).thenReturn(mockTeachings);

        profDAO.delete(profId);

        assertFalse(profStore.containsKey(profId));
        verify(teachingDAO, times(1)).delete(100);
        verify(teachingDAO, times(1)).delete(101);
    }

    @Test
    public void testSearch_PatternNameOnly() {
        // Should match "Alberto"
        List<Professor> results = profDAO.search("alb", null);
        assertEquals(1, results.size());
        assertEquals("Cardini", results.get(0).getSurname());
    }

    @Test
    public void testSearch_PatternSurnameOnly() {
        // Should match "Bellina"
        List<Professor> results = profDAO.search("bel", null);
        assertEquals(1, results.size());
        assertEquals("Lorenzo", results.get(0).getName());
    }

    @Test
    public void testSearch_PatternFullName_Ordered() {
        // Matches Name then Surname: "Alberto Car"
        List<Professor> results = profDAO.search("alberto car", null);
        assertEquals(1, results.size());
        assertEquals("Alberto", results.get(0).getName());
    }

    @Test
    public void testSearch_PatternFullName_Reversed() {
        // Matches Surname then Name: "Cardini Alb"
        List<Professor> results = profDAO.search("cardini alb", null);
        assertEquals(1, results.size());
        assertEquals("Cardini", results.get(0).getSurname());
    }

    @Test
    public void testSearch_ByCourseId() {
        int courseId = 50;
        // Mock teachings for this course to link to Prof 1 (Lorenzo) and Prof 2 (Marco)
        when(teachingDAO.getByCourse(courseId)).thenReturn(List.of(
                new Teaching(200, courseId, 1),
                new Teaching(201, courseId, 2)
        ));

        List<Professor> results = profDAO.search(null, courseId);

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(p -> p.getName().equals("Lorenzo")));
        assertTrue(results.stream().anyMatch(p -> p.getName().equals("Marco")));
        assertFalse(results.stream().anyMatch(p -> p.getName().equals("Alberto")));
    }

    @Test
    public void testSearch_PatternAndCourseIdCombined() {
        int courseId = 50;
        // Course 50 has Prof 1 and 2
        when(teachingDAO.getByCourse(courseId)).thenReturn(List.of(
                new Teaching(200, courseId, 1),
                new Teaching(201, courseId, 2)
        ));

        // Search for "Mar" within Course 50 -> Should only return Marco (Prof 2)
        List<Professor> results = profDAO.search("mar", courseId);

        assertEquals(1, results.size());
        assertEquals("Marco", results.get(0).getName());
    }

    @Test
    public void testSearch_NoMatch() {
        List<Professor> results = profDAO.search("Zebra", null);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testGet_Found() {
        int validId = 0;

        Optional<Professor> prof = profDAO.get(validId);

        assertTrue(prof.isPresent(), "Professor should be present");
        assertEquals(validId, prof.get().getId());
    }

    @Test
    public void testGet_NotFound() {
        int invalidId = profStore.size();

        Optional<Professor> prof = profDAO.get(invalidId);

        assertTrue(prof.isEmpty(), "Professor should not be present");
    }

    @Test
    public void testUpdate_Found() {
        int validId = 0;
        String newName = "Ken";
        Professor prof = profStore.get(validId);
        prof.setName(newName);

        profDAO.update(prof);

        Professor updatedProf = profStore.get(validId);
        assertEquals(newName, updatedProf.getName());
    }

    @Test
    public void testUpdate_NotFound() {
        int invalidId = profStore.size();
        int prevSize = profStore.size();
        Professor prof = new Professor(invalidId, "Not", "Present");

        profDAO.update(prof);

        assertEquals(prevSize, profStore.size());
        assertFalse(profStore.containsKey(invalidId));
    }
}