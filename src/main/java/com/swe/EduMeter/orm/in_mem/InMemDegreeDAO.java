package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.Degree;
import com.swe.EduMeter.orm.DegreeDAO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemDegreeDAO implements DegreeDAO {
    private final ConcurrentHashMap<Integer, Degree> inMemStorage = new ConcurrentHashMap<>();
    private int id = 0;

    public InMemDegreeDAO() {}

    @Override
    public Optional<Degree> getDegreeById(int id) {
        return Optional.ofNullable(inMemStorage.get(id));
    }

    @Override
    public Optional<Degree> getDegreeByName(String name) {
        return inMemStorage.values()
                .stream()
                .filter(u -> u.getName().equals(name))
                .findAny();
    }

    @Override
    public ArrayList<Degree> getAllDegrees() { return Collections.list(inMemStorage.elements()); }

    @Override
    public void addDegree(Degree degree) {
        degree.setId(id);
        inMemStorage.put(id, degree);
        id++;
    }

    @Override
    public void deleteDegreeById(int id){
        inMemStorage.remove(id);
    }

    @Override
    public boolean deleteDegreeByName(String name){
        return inMemStorage.values().removeIf(d -> d.getName().equals(name));
    }

    @Override
    public ArrayList<Degree> getAllDegreesBySchool(String school_name) {
        ArrayList<Degree> degrees = Collections.list(inMemStorage.elements());
        degrees.removeIf(d -> !d.getSchool().getName().equals(school_name));
        return degrees;
    }

    @Override
    public boolean deleteAllDegreesBySchool(String school_name) {
        return inMemStorage.values().removeIf(d -> d.getSchool().getName().equals(school_name));
    }

}
