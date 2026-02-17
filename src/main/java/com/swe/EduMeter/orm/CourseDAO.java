package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.Course;
import com.swe.EduMeter.model.Degree;
import com.swe.EduMeter.model.School;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface CourseDAO {
    int add(Course course);
    Optional<Course> get(int id);
    void update(Course course);
    void delete(int id);

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
    List<Course> search(String pattern, Integer schoolId, Integer degreeId);
}
