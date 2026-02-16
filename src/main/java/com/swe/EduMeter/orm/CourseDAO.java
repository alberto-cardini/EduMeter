package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.Course;
import com.swe.EduMeter.model.Degree;
import com.swe.EduMeter.model.School;

import java.util.ArrayList;
import java.util.Optional;

public interface CourseDAO {
    Optional<Course> getCourseById(int id);
    Optional<Course> getCourseByName(String name);
    ArrayList<Course> getAllCourses();
    ArrayList<Course> getAllCoursesBySchool(String school_name);
    ArrayList<Course> getAllCoursesByDegree(String degree_name);

    void updateCourse(int id, Course new_course);

    void deleteCourseById(int id);
    boolean deleteCourseByName(String name);
    boolean deleteAllCoursesByDegree(String degree_name);

    void addCourse(Course course);
}
