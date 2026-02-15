package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.Course;
import com.swe.EduMeter.orm.CourseDAO;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemCourseDAO implements CourseDAO {
    private final ConcurrentHashMap<Integer, Course> inMemStorage = new ConcurrentHashMap<>();
    private int id = 0;

    public InMemCourseDAO() {}

    @Override
    public Optional<Course> getCourseById(int id){
        return Optional.ofNullable(inMemStorage.get(id));
    }

    @Override
    public Optional<Course> getCourseByName(String name){
        return inMemStorage.values()
                .stream()
                .filter(u -> u.getName().equals(name))
                .findAny();
    }

    @Override
    public ArrayList<Course> getAllCourses(){
        return new ArrayList<>(inMemStorage.values());
    }

    @Override
    public void deleteCourseById(int id){
        inMemStorage.remove(id);
    }

    @Override
    public void deleteCourseByName(String name){
        inMemStorage.values().removeIf(u -> u.getName().equals(name));
    }

    @Override
    public void addCourse(Course course){
        course.setId(id);
        inMemStorage.put(id, course);
        id++;
    }

    @Override
    public void updateCourse(int id, Course new_course){
        inMemStorage.replace(id, new_course);
    }
}
