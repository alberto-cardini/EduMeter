package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.Course;
import com.swe.EduMeter.model.Teaching;
import com.swe.EduMeter.model.response.ApiObjectCreated;
import com.swe.EduMeter.model.response.ApiOk;
import com.swe.EduMeter.orm.CourseDAO;
import com.swe.EduMeter.orm.TeachingDAO;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {

    @Mock
    private CourseDAO courseDAO;

    @Mock
    private TeachingDAO teachingDAO;

    @InjectMocks
    private CourseController courseController;

    @Test
    public void testGetCourse_Found() {
        int id = 42;
        Course course = new Course(id, "Software Engineering", 1);
        when(courseDAO.get(id)).thenReturn(Optional.of(course));

        Course gotCourse = courseController.get(id);

        assertEquals(course, gotCourse);
    }

    @Test
    public void testGetCourse_NotFound() {
        int id = 42;
        when(courseDAO.get(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> courseController.get(id));

        assertEquals("Course not found", exception.getMessage());
    }

    @Test
    public void testSearchCourse_All() {
        String query = null;
        Integer schoolId = null;
        Integer degreeId = null;
        List<Course> courses = List.of(
                new Course(0, "Software Engineering", 1),
                new Course(1, "Data Structures", 1)
        );
        when(courseDAO.search(query, schoolId, degreeId)).thenReturn(courses);

        List<Course> gotCourses = courseController.search(query, schoolId, degreeId);

        assertEquals(courses.size(), gotCourses.size());
        assertEquals(courses.get(0).getName(), gotCourses.get(0).getName());
    }

    @Test
    public void testCreateCourse() {
        Course course = new Course(null, "Software Engineering", 1);
        int expectedId = 42;
        when(courseDAO.add(course)).thenReturn(expectedId);

        ApiObjectCreated response = courseController.create(course);

        assertEquals(expectedId, response.id());
        assertEquals("Course created", response.message());
        verify(courseDAO, times(1)).add(course);
    }

    @Test
    public void testDeleteCourse_Found() {
        int id = 42;
        Course course = new Course(id, "Software Engineering", 1);
        when(courseDAO.get(id)).thenReturn(Optional.of(course));

        ApiOk response = courseController.delete(id);

        assertEquals("Course deleted", response.message());
        verify(courseDAO, times(1)).delete(id);
    }

    @Test
    public void testDeleteCourse_NotFound() {
        int id = 42;
        when(courseDAO.get(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> courseController.delete(id));

        assertEquals("Course not found", exception.getMessage());
        verify(courseDAO, never()).delete(anyInt());
    }

    @Test
    public void testUpdateCourse_Found() {
        int id = 42;
        Course course = new Course(id, "Software Engineering", 1);
        when(courseDAO.get(id)).thenReturn(Optional.of(course));

        ApiOk response = courseController.update(course);

        assertEquals("Course updated", response.message());
        verify(courseDAO, times(1)).update(course);
    }

    @Test
    public void testUpdateCourse_NotFound() {
        int id = 42;
        Course course = new Course(id, "Software Engineering", 1);
        when(courseDAO.get(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> courseController.update(course));

        assertEquals("Course not found", exception.getMessage());
        verify(courseDAO, never()).update(any(Course.class));
    }

    @Test
    public void testUpdateCourse_Invalid() {
        Course course = new Course(null, "Software Engineering", 1);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> courseController.update(course));

        assertEquals("Id must be set", exception.getMessage());
        verify(courseDAO, never()).update(any(Course.class));
    }

    @Test
    public void testListTeachings() {
        int courseId = 42;
        List<Teaching> teachings = List.of(
                new Teaching(1, courseId, 101),
                new Teaching(2, courseId, 102)
        );
        when(teachingDAO.getByCourse(courseId)).thenReturn(teachings);

        List<Teaching> gotTeachings = courseController.listTeachings(courseId);

        assertEquals(teachings.size(), gotTeachings.size());
        assertEquals(teachings.get(0).getProfId(), gotTeachings.get(0).getProfId());
    }

    @Test
    public void testAddTeaching_Valid() {
        int courseId = 42;
        int profId = 101;
        int expectedTeachingId = 7;
        CourseController.AddTeachingBody body = new CourseController.AddTeachingBody(profId);

        when(teachingDAO.add(any(Teaching.class))).thenReturn(expectedTeachingId);

        ApiObjectCreated response = courseController.addTeaching(courseId, body);

        assertEquals(expectedTeachingId, response.id());
        assertEquals("Added teacher to course", response.message());
        verify(teachingDAO, times(1)).add(any(Teaching.class));
    }

    @Test
    public void testAddTeaching_InvalidBody() {
        int courseId = 42;
        // Missing profId (null)
        CourseController.AddTeachingBody body = new CourseController.AddTeachingBody(null);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> courseController.addTeaching(courseId, body));

        assertEquals("profId must be set", exception.getMessage());
        verify(teachingDAO, never()).add(any(Teaching.class));
    }

    @Test
    public void testRemoveTeaching_Found() {
        int courseId = 42;
        int teachingId = 7;
        Teaching teaching = new Teaching(teachingId, courseId, 101);
        when(teachingDAO.get(teachingId)).thenReturn(Optional.of(teaching));

        ApiOk response = courseController.removeTeaching(courseId, teachingId);

        assertEquals("Removed teacher from course", response.message());
        verify(teachingDAO, times(1)).delete(teachingId);
    }

    @Test
    public void testRemoveTeaching_NotFound() {
        int courseId = 42;
        int teachingId = 7;
        when(teachingDAO.get(teachingId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> courseController.removeTeaching(courseId, teachingId));

        assertEquals("Teaching not found", exception.getMessage());
        verify(teachingDAO, never()).delete(anyInt());
    }
}