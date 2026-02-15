package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.Degree;
import com.swe.EduMeter.orm.DegreeDAO;

import java.util.ArrayList;
import java.util.Optional;

public class PostgreDegreeDAO implements DegreeDAO {

    @Override
    public Optional<Degree> getDegreeById(int id) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Optional<Degree> getDegreeByName(String name) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ArrayList<Degree> getAllDegrees() {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void addDegree(Degree degree) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void deleteDegreeById(int id) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void deleteDegreeByName(String name) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

}
