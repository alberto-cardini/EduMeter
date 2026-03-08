package com.swe.EduMeter.orm.postgre_dao;

import com.swe.EduMeter.models.PublishedReview;
import com.swe.EduMeter.models.Teaching;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class PostgreTeachingDAO_IT extends PostgreIT {
    private final PostgreProfDAO profDAO = new PostgreProfDAO();
    private final PostgreTeachingDAO teachingDAO = new PostgreTeachingDAO();

    public static void insertTeachings() throws SQLException {
        Connection c = DatabaseManager.getInstance().getConnection();
        // 5 prof, 5 corsi
        String statement = "INSERT INTO Teaching (course_id, professor_id) VALUES" +
                "    (1, 1)," +
                "    (1, 2)," +
                "    (2, 1)," +
                "    (2, 2)," +
                "    (2, 3)," +
                "    (3, 4)," +
                "    (4, 4)," +
                "    (4, 5)," +
                "    (5, 4)," +
                "    (5, 5)," +
                "    (5, 1)";

        try (PreparedStatement st = c.prepareStatement(statement)) {
            st.execute();
        }
    }
    @BeforeEach
    public void setup() throws SQLException {
        PostgreSchoolDAO_IT.insertSchools();
        PostgreDegreeDAO_IT.insertDegrees();
        PostgreCourseDAO_IT.insertCourses();
        PostgreProfDAO_IT.insertProfessors();
        insertTeachings();
    }

    //@Test
    //public void testAdd_Success() {
        //int courseId = 5;
        //int profId = 2;
        //Teaching newTeaching = new Teaching(null, courseId, profId);

        //int generatedId = teachingDAO.add(newTeaching);

        //Optional<Teaching> insertedTeaching = teachingDAO.get(generatedId);

        //assertTrue(insertedTeaching.isPresent(), "Teaching should be present");
        //assertEquals(courseId, insertedTeaching.get().getCourseId());
        //assertEquals(profId, insertedTeaching.get().getProfId());
    //}

    //@Test
    //public void testAdd_InvalidProf() {
        //int invalidProfId = 999;
        //Teaching newTeaching = new Teaching(null, 1, invalidProfId);

        //assertThrows(RuntimeException.class, () -> teachingDAO.add(newTeaching));
    //}

    //@Test
    //public void testAdd_InvalidCourse() {
        //int invalidCourseId = 999;
        //Teaching newTeaching = new Teaching(null, invalidCourseId, 1);

        //assertThrows(RuntimeException.class, () -> teachingDAO.add(newTeaching));
    //}

    //@Test
    //public void testDelete_CascadesToReviews() {
        //int teachingId = 0;
        //Teaching t = store.get(teachingId);

        //// Mock the reviews linked to this specific Course/Prof combo
        //PublishedReview r1 = new PublishedReview(); r1.setId(500);
        //PublishedReview r2 = new PublishedReview(); r2.setId(501);

        //when(reviewDAO.search(null, null, t.getCourseId(), t.getProfId(), null))
                //.thenReturn(List.of(r1, r2));

        //teachingDAO.delete(teachingId);

        //assertFalse(store.containsKey(teachingId));
        //// Verify reviews were deleted
        //verify(reviewDAO).delete(500);
        //verify(reviewDAO).delete(501);
    //}

    //@Test
    //public void testDelete_NotFound_DoesNothing() {
        //teachingDAO.delete(99);
        //verifyNoInteractions(reviewDAO);
    //}

    //@Test
    //public void testGetByCourse() {
        //List<Teaching> results = teachingDAO.getByCourse(10);
        //assertEquals(2, results.size());
        //assertTrue(results.stream().allMatch(t -> t.getCourseId() == 10));
    //}

    //@Test
    //public void testGetByProf() {
        //List<Teaching> results = teachingDAO.getByProf(100);
        //assertEquals(2, results.size());
        //assertTrue(results.stream().allMatch(t -> t.getProfId() == 100));
    //}

    //@Test
    //public void testGet_Found() {
        //int validId = 0;

        //Optional<Teaching> teaching = teachingDAO.get(validId);

        //assertTrue(teaching.isPresent(), "Teaching should be present");
        //assertEquals(validId, teaching.get().getId());
    //}

    //@Test
    //public void testGet_NotFound() {
        //int invalidId = store.size();

        //Optional<Teaching> teaching = teachingDAO.get(invalidId);

        //assertTrue(teaching.isEmpty(), "Teaching should not be present");
    //}
}
