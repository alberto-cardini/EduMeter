package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.Course;
import com.swe.EduMeter.model.Degree;
import com.swe.EduMeter.model.School;
import com.swe.EduMeter.orm.CourseDAO;
import com.swe.EduMeter.orm.DegreeDAO;
import com.swe.EduMeter.orm.SchoolDAO;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemDegreeDAO implements DegreeDAO {
    private final ConcurrentHashMap<Integer, Degree> inMemStorage = new ConcurrentHashMap<>();
    private int id = 0;

    public InMemDegreeDAO() {
        add(new Degree(null, "computer-engineering", Degree.Type.Bachelor, 0));
        add(new Degree(null, "law", Degree.Type.Bachelor, 1));
        add(new Degree(null, "medicine", Degree.Type.Master, 2));
    }

    @Override
    public int add(Degree degree) {
        new InMemDAOFactory()
                .getSchoolDAO()
                .get(degree.getSchoolId())
                .orElseThrow(() -> new RuntimeException("Invalid schoolId in the degree init JSON"));
        degree.setId(id);
        inMemStorage.put(id++, degree);
        return degree.getId();
    }

    @Override
    public Optional<Degree> get(int id) {
        return Optional.ofNullable(inMemStorage.get(id));
    }

    @Override
    public void update(Degree degree) {
        inMemStorage.replace(degree.getId(), degree);
    }

    @Override
    public void delete(int id){
        inMemStorage.remove(id);

        CourseDAO courseDAO = new InMemDAOFactory().getCourseDAO();

        // InMemDAOFactory returns references to static DAOs. This is
        // useful to replicate DB behaviours, like cascade deletion.
        List<Course> courses = courseDAO.search(null, null, id);

        for (Course c: courses) {
            courseDAO.delete(c.getId());
        }
    }

    @Override
    public List<Degree> search(String pattern, Integer schoolId) {
        return inMemStorage.values()
                .stream()
                // filter by pattern (if pattern exists)
                .filter(d -> pattern == null || d.getName().toLowerCase().contains(pattern.toLowerCase()))
                // filter by school (if schoolId exists)
                .filter(d -> schoolId == null || d.getSchoolId() == schoolId)
                .collect(Collectors.toList());
    }

}
