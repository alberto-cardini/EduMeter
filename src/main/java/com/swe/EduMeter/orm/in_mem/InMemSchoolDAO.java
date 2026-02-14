package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.orm.SchoolDAO;
import com.swe.EduMeter.model.School;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemSchoolDAO implements SchoolDAO {
    private final ConcurrentHashMap<Integer, School> inMemStorage = new ConcurrentHashMap<>();
    private int id = 0;

    public InMemSchoolDAO() {
        addSchool(new School(0, "school-of-engineering"));
        addSchool(new School(0, "school-of-law"));
        addSchool(new School(0, "medical-school"));
    }

    @Override
    public Optional<School> getSchoolById(int id) {
        return Optional.ofNullable(inMemStorage.get(id));
    }

    @Override
    public Optional<School> getSchoolByName(String name) {
        return inMemStorage.values()
                .stream()
                .filter(u -> u.getName().equals(name))
                .findAny();
    }

    @Override
    public void addSchool(School school){
        if(!inMemStorage.containsKey(school.getId())) {
            school.setId(id);
            inMemStorage.put(id, school);
            id++;
        }else{
            //TODO: manage the error for the insertion of an already existing school with that ID.
            //      Throw an exception maybe.
        }
    }

    @Override
    public void deleteSchoolById(int id){
        inMemStorage.remove(id);
    }

    @Override
    public void deleteSchoolByName(String name){
        inMemStorage.values().removeIf(u -> u.getName().equals(name));
    }

}
