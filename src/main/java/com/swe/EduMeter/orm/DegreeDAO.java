package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.Degree;
import com.swe.EduMeter.model.School;

import java.util.ArrayList;
import java.util.Optional;

public interface DegreeDAO {
    Optional<Degree> getDegreeById(int id);
    Optional<Degree> getDegreeByName(String name);
    ArrayList<Degree> getAllDegrees();
    ArrayList<Degree> getAllDegreesBySchool(String school_name);

    void deleteDegreeById(int id);
    boolean deleteDegreeByName(String name);
    boolean deleteAllDegreesBySchool(String school_name);

    void addDegree(Degree degree);
}
