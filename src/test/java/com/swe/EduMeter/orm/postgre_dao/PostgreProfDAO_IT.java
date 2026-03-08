package com.swe.EduMeter.orm.postgre_dao;

import com.swe.EduMeter.models.Professor;
import com.swe.EduMeter.models.Teaching;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PostgreProfDAO_IT extends PostgreIT {
    private final PostgreProfDAO profDAO = new PostgreProfDAO();
    private final PostgreTeachingDAO teachingDAO = new PostgreTeachingDAO();

    public static void insertProfessors() throws SQLException {
        Connection c = DatabaseManager.getInstance().getConnection();
        String statement = "INSERT INTO Professor (name, surname) VALUES" +
                "    ('Mark', 'Knopfler')," +
                "    ('David', 'Gilmour')," +
                "    ('Jimi', 'Hendrix')," +
                "    ('Steve', 'Hackett')," +
                "    ('George', 'Harrison');";

        try (PreparedStatement st = c.prepareStatement(statement)) {
            st.execute();
        }
    }
    @BeforeEach
    public void setup() throws SQLException {
        insertProfessors();
    }

    @Test
    public void testAdd() {
        Professor prof = new Professor(null, "Eddie", "Van Halen");
        int newId = profDAO.add(prof);

        Optional<Professor> insertedProf = profDAO.get(newId);

        assertTrue(insertedProf.isPresent(), "Professor should be present");
        assertEquals("Eddie", insertedProf.get().getName());
    }

    @Test
    public void testDelete_CascadesToTeachings() throws SQLException {
        PostgreSchoolDAO_IT.insertSchools();
        PostgreDegreeDAO_IT.insertDegrees();
        PostgreCourseDAO_IT.insertCourses();
        PostgreTeachingDAO_IT.insertTeachings();
        int profId = 1;

        profDAO.delete(profId);
        Optional<Professor> deletedProfessor = profDAO.get(profId);
        List<Teaching> deletedTeachings = teachingDAO.getByProf(profId);

        assertTrue(deletedProfessor.isEmpty(), "Professor should be deleted");
        assertTrue(deletedTeachings.isEmpty(), "Teachings should be cascade deleted");
    }

    @Test
    public void testSearch_PatternNameOnly() {
        // Should match "Mark"
        List<Professor> results = profDAO.search("ark", null);
        assertEquals(1, results.size());
        assertEquals("Knopfler", results.get(0).getSurname());
    }

    @Test
    public void testSearch_PatternSurnameOnly() {
        // Should match "Hackett"
        List<Professor> results = profDAO.search("Hack", null);
        assertEquals(1, results.size());
        assertEquals("Steve", results.get(0).getName());
    }

    @Test
    public void testSearch_PatternFullName_Ordered() {
        // Matches Name then Surname: "Mark Knopfler"
        List<Professor> results = profDAO.search("ARK knop", null);
        assertEquals(1, results.size());
        assertEquals("Mark", results.get(0).getName());
    }

    @Test
    public void testSearch_PatternFullName_Reversed() {
        // Matches Surname then Name: "Knopfler Mark"
        List<Professor> results = profDAO.search("fler MAR", null);
        assertEquals(1, results.size());
        assertEquals("Knopfler", results.get(0).getSurname());
    }

    @Test
    public void testSearch_ByCourseId() throws SQLException {
        PostgreSchoolDAO_IT.insertSchools();
        PostgreDegreeDAO_IT.insertDegrees();
        PostgreCourseDAO_IT.insertCourses();
        PostgreTeachingDAO_IT.insertTeachings();
        int courseId = 2;

        List<Professor> results = profDAO.search(null, courseId);

        assertEquals(3, results.size());
        assertTrue(results.stream().anyMatch(p -> p.getName().equals("Mark")));
        assertTrue(results.stream().anyMatch(p -> p.getName().equals("David")));
        assertTrue(results.stream().anyMatch(p -> p.getName().equals("Jimi")));
    }

    @Test
    public void testSearch_PatternAndCourseId() throws SQLException {
        PostgreSchoolDAO_IT.insertSchools();
        PostgreDegreeDAO_IT.insertDegrees();
        PostgreCourseDAO_IT.insertCourses();
        PostgreTeachingDAO_IT.insertTeachings();

        // Course 2 has Prof 1, 2 and 3
        int courseId = 2;

        // Search for "Mar" within Course 2 -> Should only return Mark
        List<Professor> results = profDAO.search("mar", courseId);

        assertEquals(1, results.size());
        assertEquals("Mark", results.get(0).getName());
    }

    @Test
    public void testSearch_NoMatch() {
        List<Professor> results = profDAO.search("Zebra", null);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testGet_Found() {
        int validId = 1;

        Optional<Professor> prof = profDAO.get(validId);

        assertTrue(prof.isPresent(), "Professor should be present");
        assertEquals("Mark", prof.get().getName());
    }

    @Test
    public void testGet_NotFound() {
        int invalidId = 999;

        Optional<Professor> prof = profDAO.get(invalidId);

        assertTrue(prof.isEmpty(), "Professor should not be present");
    }

    @Test
    public void testUpdate_Found() {
        int validId = 1;
        String newName = "David";
        Professor prof = profDAO.get(validId)
                .orElseThrow(() -> new RuntimeException("Professor should be present"));
        prof.setName(newName);

        profDAO.update(prof);

        Optional<Professor> updatedProfessor = profDAO.get(validId);

        assertTrue(updatedProfessor.isPresent(), "Professor should be present");
        assertEquals(newName, updatedProfessor.get().getName());
    }

    @Test
    public void testUpdate_NotFound() {
        int invalidId = 999;
        Professor prof = new Professor(invalidId, "Not", "Present");

        profDAO.update(prof);

        Optional<Professor> invalidProfessor = profDAO.get(invalidId);

        assertTrue(invalidProfessor.isEmpty(), "Professor should not be present");
    }
}
