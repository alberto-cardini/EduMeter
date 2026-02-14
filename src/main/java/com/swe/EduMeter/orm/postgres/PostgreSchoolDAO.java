package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.School;
import com.swe.EduMeter.orm.SchoolDAO;

import java.util.Optional;

public class PostgreSchoolDAO implements SchoolDAO {
    @Override
    public Optional<School> getSchoolById(int id) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Optional<School> getSchoolByName(String name) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void addSchool(School school) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void deleteSchoolById(int id) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void deleteSchoolByName(String name) {
        // TODO
        throw new RuntimeException("Not implemented");
    }
}