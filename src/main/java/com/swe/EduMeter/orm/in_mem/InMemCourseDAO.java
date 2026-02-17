package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.Course;
import com.swe.EduMeter.orm.CourseDAO;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemCourseDAO implements CourseDAO {
    private final ConcurrentHashMap<Integer, Course> inMemStorage = new ConcurrentHashMap<>();
    private int id = 0;

    public InMemCourseDAO() {}

    @Override
    public int add(Course course){
        course.setId(id);
        inMemStorage.put(id, course);
        id++;

        return course.getId();
    }

    @Override
    public Optional<Course> get(int id){
        return Optional.ofNullable(inMemStorage.get(id));
    }

    @Override
    public void update(Course course){
        inMemStorage.replace(course.getId(), course);
    }

    @Override
    public void delete(int id){
        inMemStorage.remove(id);
    }

    @Override
    public List<Course> search(String pattern, Integer schoolId, Integer courseId){
        return inMemStorage.values()
                .stream()
                // filter by pattern (if pattern exists)
                .filter(c -> pattern == null || c.getName().toLowerCase().contains(pattern.toLowerCase()))
                // filter by school (if schoolId exists)
                .filter(c -> schoolId == null || c.getDegree().getSchool().getId().equals(schoolId))
                // filter by course (if courseId exists)
                .filter(c -> courseId == null || c.getDegree().getId().equals(courseId))
                .collect(Collectors.toList());
    }
}
