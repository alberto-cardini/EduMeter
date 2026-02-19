package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.Degree;
import com.swe.EduMeter.orm.DegreeDAO;
import com.swe.EduMeter.orm.SchoolDAO;
import com.swe.EduMeter.model.School;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemSchoolDAO implements SchoolDAO {
    private final ConcurrentHashMap<Integer, School> inMemStorage = new ConcurrentHashMap<>();
    private int id = 0;

    public InMemSchoolDAO() {
        add(new School(null, "school-of-engineering"));
        add(new School(null, "school-of-law"));
        add(new School(null, "medical-school"));
    }

    @Override
    public int add(School school) {
        school.setId(id);
        inMemStorage.put(id, school);
        id++;

        return school.getId();
    }
    @Override
    public Optional<School> get(int id) {
        return Optional.ofNullable(inMemStorage.get(id));
    }

    @Override
    public void update(School school) {
        inMemStorage.put(school.getId(), school);
    }

    @Override
    public void delete(int id){
        inMemStorage.remove(id);

        DegreeDAO degreeDAO = new InMemDAOFactory().getDegreeDAO();

        // InMemDAOFactory returns references to static DAOs. This is
        // useful to replicate DB behaviours, like cascade deletion.
        List<Degree> degrees = degreeDAO.search(null, id);

        for (Degree d: degrees) {
            degreeDAO.delete(d.getId());
        }
    }

    @Override
    public List<School> search(String pattern) {
        return inMemStorage.values()
                .stream()
                // filter by pattern (if pattern exists)
                .filter(s -> pattern == null || s.getName().toLowerCase().contains(pattern.toLowerCase()))
                .collect(Collectors.toList());
    }
}
