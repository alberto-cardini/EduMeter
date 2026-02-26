package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.Course;
import com.swe.EduMeter.model.Degree;
import com.swe.EduMeter.orm.CourseDAO;
import com.swe.EduMeter.orm.DegreeDAO;
import com.swe.EduMeter.orm.SchoolDAO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemDegreeDAO implements DegreeDAO {
    private final Map<Integer, Degree> store;
    private final CourseDAO courseDAO;
    private final SchoolDAO schoolDAO;
    private int id = 0;

    public InMemDegreeDAO(Map<Integer, Degree> store, CourseDAO courseDAO,
                          SchoolDAO schoolDAO) {
        this.store = store;
        this.courseDAO = courseDAO;
        this.schoolDAO = schoolDAO;
        //add(new Degree(null, "Computer Engineering", Degree.Type.Bachelor, 0));
        //add(new Degree(null, "Law", Degree.Type.Bachelor, 1));
        //add(new Degree(null, "Medicine", Degree.Type.Master, 2));
    }

    @Override
    public int add(Degree degree) {
        schoolDAO
            .get(degree.getSchoolId())
            .orElseThrow(() -> new RuntimeException("Invalid schoolId"));

        degree.setId(id);
        store.put(id++, degree);
        return degree.getId();
    }

    @Override
    public Optional<Degree> get(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void update(Degree degree) {
        store.replace(degree.getId(), degree);
    }

    @Override
    public void delete(int id){
        store.remove(id);

        List<Course> courses = courseDAO.search(null, null, id);

        for (Course c: courses) {
            courseDAO.delete(c.getId());
        }
    }

    @Override
    public List<Degree> search(String pattern, Integer schoolId) {
        return store.values()
                .stream()
                // filter by pattern (if pattern exists)
                .filter(d -> pattern == null || d.getName().toLowerCase().contains(pattern.toLowerCase()))
                // filter by school (if schoolId exists)
                .filter(d -> schoolId == null || d.getSchoolId() == schoolId)
                .collect(Collectors.toList());
    }

}
