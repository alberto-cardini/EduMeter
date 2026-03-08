package com.swe.EduMeter.orm.postgre_dao;

import com.swe.EduMeter.models.Course;
import com.swe.EduMeter.models.Degree;
import com.swe.EduMeter.models.School;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostgreDegreeDAO_IT extends PostgreIT {
    private final PostgreDegreeDAO degreeDAO = new PostgreDegreeDAO();
    private final PostgreCourseDAO courseDAO = new PostgreCourseDAO();

    public static void insertDegrees() throws SQLException {
        Connection c = DatabaseManager.getInstance().getConnection();
        String statement = "INSERT INTO Degree (name, type, school_id) VALUES" +
                "    ('Computer Engineering', 'Bachelor', 1)," +
                "    ('Law', 'Bachelor', 2)," +
                "    ('Biology', 'Master', 3);";

        try (PreparedStatement st = c.prepareStatement(statement)) {
            st.execute();
        }
    }
    @BeforeEach
    public void setup() throws SQLException {
        PostgreSchoolDAO_IT.insertSchools();
        insertDegrees();
    }

    @Test
    public void testGet_Found() {
        int validId = 1;
        Optional<Degree> degree = degreeDAO.get(validId);

        assertTrue(degree.isPresent());
        assertEquals("Computer Engineering", degree.get().getName());
    }

    @Test
    public void testGet_NotFound() {
        int invalidId = 999;
        Optional<Degree> degree = degreeDAO.get(invalidId);

        assertTrue(degree.isEmpty());
    }

    @Test
    public void testSearch_ByPatternAndSchool() {
        // Search for "Law" in School 1
        List<Degree> results = degreeDAO.search("law", 2);
        assertEquals(1, results.size());
        assertEquals("Law", results.get(0).getName());

        // Search for "Law" in School 2
        List<Degree> emptyResults = degreeDAO.search("law", 3);
        assertTrue(emptyResults.isEmpty());
    }

    @Test
    public void testAdd_Success() {
        int schoolId = 1;
        String courseName = "Space Engineering";
        Degree newDegree = new Degree(null, courseName, Degree.Type.Master, schoolId);

        int generatedId = degreeDAO.add(newDegree);
        Optional<Degree> insertedDegree = degreeDAO.get(generatedId);

        assertTrue(insertedDegree.isPresent(), "Degree should be inserted");
        assertEquals(courseName, insertedDegree.get().getName());
    }

    @Test
    public void testAdd_InvalidSchool() {
        int invalidSchoolId = 999;
        Degree newDegree = new Degree(null, "Ghost Degree", Degree.Type.Bachelor, invalidSchoolId);

        assertThrows(RuntimeException.class, () -> degreeDAO.add(newDegree));
    }

    @Test
    public void testUpdate() {
        int validId = 1;
        Degree degree = degreeDAO.get(validId)
                .orElseThrow(() -> new RuntimeException("Degree should be present"));
        String newName = "Updated Name";
        degree.setName(newName);

        degreeDAO.update(degree);
        Optional<Degree> updatedDegree = degreeDAO.get(validId);

        assertTrue(updatedDegree.isPresent(), "Degree should be present");
        assertEquals(newName, updatedDegree.get().getName());
    }

    @Test
    public void testDelete_CascadesToCourses() throws SQLException {
        PostgreCourseDAO_IT.insertCourses();
        int degreeIdToDelete = 1;

        degreeDAO.delete(degreeIdToDelete);
        Optional<Degree> deletedDegree = degreeDAO.get(degreeIdToDelete);
        List<Course> deletedCourses = courseDAO.search(null, null, degreeIdToDelete);

        assertTrue(deletedDegree.isEmpty(), "Degree should be deleted");
        assertTrue(deletedCourses.isEmpty(), "Courses should be cascade deleted");
    }
}
