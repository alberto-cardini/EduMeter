package com.swe.EduMeter.orm.postgre_dao;

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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostgreSchoolDAO_IT extends PostgreIT {
    private final PostgreSchoolDAO schoolDAO = new PostgreSchoolDAO();
    private final PostgreDegreeDAO degreeDAO = new PostgreDegreeDAO();

    public static void insertSchools() throws SQLException {
        Connection c = DatabaseManager.getInstance().getConnection();
        String statement = "INSERT INTO School (name) VALUES" +
                "    ('School of Engineering')," +
                "    ('School of Law')," +
                "    ('School of Science');";

        try (PreparedStatement st = c.prepareStatement(statement)) {
            st.execute();
        }
    }
    @BeforeEach
    public void setup() throws SQLException {
        insertSchools();
    }

    @Test
    public void testGet_Found() {
        int validId = 1;

        Optional<School> school = schoolDAO.get(validId);

        assertTrue(school.isPresent(), "School should be present");
        assertEquals(validId, school.get().getId());
        assertEquals("School of Engineering", school.get().getName());
    }

    @Test
    public void testGet_NotFound() {
        int invalidId = 999;

        Optional<School> school = schoolDAO.get(invalidId);

        assertTrue(school.isEmpty(), "School should not be present");
    }

    @Test
    public void testSearch_All() {
        String query = null;

        List<School> gotSchools = schoolDAO.search(query);

        assertEquals(3, gotSchools.size());
    }

    @Test
    public void testSearch_Filter() {
        String query = "ool of Eng";

        List<School> gotSchools = schoolDAO.search(query);

        assertEquals(1, gotSchools.size());
        assertEquals("School of Engineering", gotSchools.get(0).getName());
    }

    @Test
    public void testSearch_Empty() {
        String query = "This certainly doesn't match any school";

        List<School> gotSchools = schoolDAO.search(query);

        assertEquals(0, gotSchools.size());
    }

    @Test
    public void testAdd() {
        String schoolName = "My new School TEST";
        School newSchool = new School(null, schoolName);

        int gotId = schoolDAO.add(newSchool);
        Optional<School> gotSchool = schoolDAO.get(gotId);

        assertTrue(gotSchool.isPresent(), "School should be inserted");
        assertEquals(schoolName, gotSchool.get().getName());
    }

    @Test
    public void TestUpdateSchool_Found() {
        School school = schoolDAO.get(1)
                .orElseThrow(() -> new RuntimeException("School should be present"));

        String updatedName = "School of TEST UPDATE";
        school.setName(updatedName);

        schoolDAO.update(school);

        Optional<School> updatedSchool = schoolDAO.get(school.getId());

        assertTrue(updatedSchool.isPresent(), "School should be present");
        assertEquals(updatedName, updatedSchool.get().getName());
    }

    @Test
    public void TestUpdateSchool_NotFound() {
        int invalidId = 999;
        School school = new School(invalidId, "School with invalid ID");

        schoolDAO.update(school);

        Optional<School> gotSchool = schoolDAO.get(invalidId);

        assertTrue(gotSchool.isEmpty(), "School should not be present");
    }

    @Test
    public void TestDeleteSchool_Found() throws SQLException {
        PostgreDegreeDAO_IT.insertDegrees();

        schoolDAO.delete(1);

        Optional<School> deletedSchool = schoolDAO.get(1);
        Optional<Degree> deletedDegree = degreeDAO.get(1);

        assertTrue(deletedSchool.isEmpty(), "School should not be present");
        assertTrue(deletedDegree.isEmpty(), "Degree should be cascade deleted");
    }

    @Test
    public void TestDeleteSchool_NotFound() {
        int invalidId = 999;

        assertDoesNotThrow(() -> schoolDAO.delete(invalidId));
    }
}
