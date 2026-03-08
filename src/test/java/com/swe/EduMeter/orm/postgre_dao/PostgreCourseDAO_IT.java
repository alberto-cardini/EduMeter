package com.swe.EduMeter.orm.postgre_dao;

import com.swe.EduMeter.models.Course;
import com.swe.EduMeter.models.Degree;
import com.swe.EduMeter.models.Teaching;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class PostgreCourseDAO_IT extends PostgreIT {
    private final PostgreCourseDAO courseDAO = new PostgreCourseDAO();
    private final PostgreTeachingDAO teachingDAO = new PostgreTeachingDAO();

    public static void insertCourses() throws SQLException {
        Connection c = DatabaseManager.getInstance().getConnection();
        String statement = "INSERT INTO Course (name, degree_id) VALUES" +
                "    ('Algorithms and Datastructures', 1)," +
                "    ('Internet', 1)," +
                "    ('International Laws', 2)," +
                "    ('Anatomy', 3)," +
                "    ('Fisiology', 3);";

        try (PreparedStatement st = c.prepareStatement(statement)) {
            st.execute();
        }
    }
    @BeforeEach
    public void setup() throws SQLException {
        PostgreSchoolDAO_IT.insertSchools();
        PostgreDegreeDAO_IT.insertDegrees();
        insertCourses();
    }

    @Test
    public void testGet_Found() {
        int validId = 1;
        Optional<Course> course = courseDAO.get(validId);

        assertTrue(course.isPresent(), "Course should be present");
        assertEquals("Algorithms and Datastructures", course.get().getName());
    }

    @Test
    public void testGet_NotFound() {
        int invalidId = 999;
        Optional<Course> course = courseDAO.get(invalidId);

        assertTrue(course.isEmpty(), "Course should not be present");
    }

    @Test
    public void testAdd_Success() {
        int degreeId = 1;
        String courseName = "Operating Systems";
        Course newCourse = new Course(null, courseName, degreeId);

        int newId = courseDAO.add(newCourse);
        Optional<Course> insertedCourse = courseDAO.get(newId);

        assertTrue(insertedCourse.isPresent(), "Course should be inserted");
        assertEquals(courseName, insertedCourse.get().getName());
    }

    @Test
    public void testAddCourse_InvalidDegree() {
        int invalidDegreeId = 999;
        Course newCourse = new Course(null, "Fake Course", invalidDegreeId);

        assertThrows(RuntimeException.class, () -> courseDAO.add(newCourse));
    }

    @Test
    public void testDeleteCourse_CascadesToTeachings() throws SQLException {
        PostgreProfDAO_IT.insertProfessors();
        PostgreTeachingDAO_IT.insertTeachings();
        int courseId = 1;

        courseDAO.delete(courseId);

        Optional<Course> deletedCourse = courseDAO.get(courseId);
        List<Teaching> deletedTeachings = teachingDAO.getByCourse(courseId);

        assertTrue(deletedCourse.isEmpty(), "Course should be deleted");
        assertTrue(deletedTeachings.isEmpty(), "Teachings should be cascade deleted");
    }

    @Test
    public void testSearch_ByPattern() {
        List<Course> results = courseDAO.search("data", null, null);
        assertEquals(1, results.size());
        assertEquals("Algorithms and Datastructures", results.get(0).getName());
    }

    @Test
    public void testSearch_ByDegreeId() {
        List<Course> results = courseDAO.search(null, null, 1);
        assertEquals(2, results.size());
    }

    @Test
    public void testSearch_BySchoolId() {
        int schoolId = 1;

        List<Course> results = courseDAO.search(null, schoolId, null);

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(c -> c.getDegreeId() == 1));
    }

    @Test
    public void testUpdate_Found() {
        int courseId = 1;
        Course course = courseDAO.get(courseId)
                .orElseThrow(() -> new RuntimeException("Course should be present"));
        String newName = "new course name";
        course.setName(newName);

        courseDAO.update(course);

        Optional<Course> updatedCourse = courseDAO.get(courseId);

        assertTrue(updatedCourse.isPresent(), "Course should be present");
        assertEquals(newName, updatedCourse.get().getName());
    }

    @Test
    public void testUpdate_NotFound() {
        Course invalidCourse = new Course(999, "Invalid course", 1);

        courseDAO.update(invalidCourse);

        Optional<Course> updatedCourse = courseDAO.get(invalidCourse.getId());

        assertTrue(updatedCourse.isEmpty(), "Course should not be present");
    }
}
