package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.Course;
import com.swe.EduMeter.orm.CourseDAO;

import java.util.ArrayList;
import java.util.Optional;

public class PostgreCourseDAO implements CourseDAO {

    @Override
    public Optional<Course> getCourseById(int id) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Optional<Course> getCourseByName(String name) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ArrayList<Course> getAllCourses() {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void addCourse(Course course) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void deleteCourseById(int id) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean deleteCourseByName(String name) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void updateCourse(int id, Course new_course) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ArrayList<Course> getAllCoursesBySchool(String school_name) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ArrayList<Course> getAllCoursesByDegree(String degree_name) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean deleteAllCoursesByDegree(String degree_name) {
        // TODO
        throw new RuntimeException("Not implemented");
    }
}
