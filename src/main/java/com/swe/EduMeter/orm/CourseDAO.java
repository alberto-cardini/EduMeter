package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.Course;

import java.util.ArrayList;
import java.util.Optional;

public interface CourseDAO {
    Optional<Course> getCourseById(int id);
    Optional<Course> getCourseByName(String name);
    ArrayList<Course> getAllCourses();

    void updateCourse(int id, Course new_course);

    void deleteCourseById(int id);
    void deleteCourseByName(String name);

    void addCourse(Course course);
}
