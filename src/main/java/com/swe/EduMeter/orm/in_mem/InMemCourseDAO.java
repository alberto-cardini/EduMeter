package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.Course;
import com.swe.EduMeter.model.Degree;
import com.swe.EduMeter.model.Teaching;
import com.swe.EduMeter.orm.CourseDAO;
import com.swe.EduMeter.orm.DegreeDAO;
import com.swe.EduMeter.orm.TeachingDAO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemCourseDAO implements CourseDAO {
    private final Map<Integer, Course> store;
    private final TeachingDAO teachingDAO;
    private final DegreeDAO degreeDAO;
    private int id = 0;

    public InMemCourseDAO(Map<Integer, Course> store, TeachingDAO teachingDAO,
                          DegreeDAO degreeDAO) {
        this.store = store;
        this.teachingDAO = teachingDAO;
        this.degreeDAO = degreeDAO;
        //add(new Course(null, "Algorithms and Data Structures", 0));
        //add(new Course(null, "Statistics", 0));
        //add(new Course(null, "Diritto privato", 1));
        //add(new Course(null, "Diritto penale", 1));
        //add(new Course(null, "Anatomy", 2));
        //add(new Course(null, "Neurology", 2));
    }

    @Override
    public Integer add(Course course){
        degreeDAO
            .get(course.getDegreeId())
            .orElseThrow(() -> new RuntimeException("Invalid degreeId"));

        course.setId(id);
        store.put(id++, course);
        return course.getId();
    }

    @Override
    public Optional<Course> get(int id){
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void update(Course course){
        store.replace(course.getId(), course);
    }

    @Override
    public void delete(int id){
        store.remove(id);

        for (Teaching t: teachingDAO.getByCourse(id)) {
            teachingDAO.delete(t.getId());
        }
    }

    @Override
    public List<Course> search(String pattern, Integer schoolId, Integer degreeId) {
        return store.values()
                .stream()
                // filter by pattern (if pattern exists)
                .filter(c -> pattern == null || c.getName().toLowerCase().contains(pattern.toLowerCase()))
                // filter by school (if schoolId exists)
                .filter(c -> {
                    if (schoolId == null) return true;

                    Degree d = degreeDAO.get(c.getDegreeId())
                                        .orElseThrow(() -> new RuntimeException("Invalid degreeId of: " + c));

                    return d.getSchoolId() == schoolId;
                })
                // filter by course (if courseId exists)
                .filter(c -> degreeId == null || c.getDegreeId() == degreeId)
                .collect(Collectors.toList());
    }
}
