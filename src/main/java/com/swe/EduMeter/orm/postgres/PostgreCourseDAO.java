package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.Course;
import com.swe.EduMeter.orm.CourseDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgreCourseDAO implements CourseDAO {

    @Override
    public int add(Course course) {
        // TODO
        throw new RuntimeException("Not implemented");
    };

    @Override
    public Optional<Course> get(int id) {
        // TODO
        throw new RuntimeException("Not implemented");
    };

    @Override
    public void update(Course course) {
        // TODO
        throw new RuntimeException("Not implemented");
    };

    @Override
    public void delete(int id) {
        // TODO
        throw new RuntimeException("Not implemented");
    };

    /**
     * @param pattern  The pattern to look for. A null
     *                 does not apply a name filter.
     * @param schoolId The school to filter for. A null
     *                 schoolId looks for courses of
     *                 every school.
     * @param degreeId The degree to filter for. A null
     *                 degreeId looks for courses of
     *                 every degree.
     *
     * @return        List of degrees
     */
    @Override
    public List<Course> search(String pattern, Integer schoolId, Integer degreeId) {
        // TODO
        throw new RuntimeException("Not implemented");
    };

}
