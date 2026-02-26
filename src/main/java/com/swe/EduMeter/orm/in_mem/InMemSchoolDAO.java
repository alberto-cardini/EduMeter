package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.Degree;
import com.swe.EduMeter.orm.DegreeDAO;
import com.swe.EduMeter.orm.SchoolDAO;
import com.swe.EduMeter.model.School;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemSchoolDAO implements SchoolDAO {
    private final Map<Integer, School> store;
    private final DegreeDAO degreeDAO;
    private int id = 0;

    public InMemSchoolDAO(Map<Integer, School> store, DegreeDAO degreeDAO) {
        this.store = store;
        this.degreeDAO = degreeDAO;
        //add(new School(null, "School of Engineering"));
        //add(new School(null, "School of Law"));
        //add(new School(null, "Medical School"));
    }

    @Override
    public int add(School school) {
        school.setId(id);
        store.put(id, school);
        id++;

        return school.getId();
    }
    @Override
    public Optional<School> get(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void update(School school) {
        store.replace(school.getId(), school);
    }

    @Override
    public void delete(int id){
        store.remove(id);

        List<Degree> degrees = degreeDAO.search(null, id);

        for (Degree d: degrees) {
            degreeDAO.delete(d.getId());
        }
    }

    @Override
    public List<School> search(String pattern) {
        return store.values()
                .stream()
                // filter by pattern (if pattern exists)
                .filter(s -> pattern == null || s.getName().toLowerCase().contains(pattern.toLowerCase()))
                .collect(Collectors.toList());
    }
}
