package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.Degree;

import java.util.ArrayList;
import java.util.Optional;

public interface DegreeDAO {
    Optional<Degree> getDegreeById(int id);
    Optional<Degree> getDegreeByName(String name);
    ArrayList<Degree> getAllDegrees();

    void deleteDegreeById(int id);
    void deleteDegreeByName(String name);

    void addDegree(Degree degree);
}
