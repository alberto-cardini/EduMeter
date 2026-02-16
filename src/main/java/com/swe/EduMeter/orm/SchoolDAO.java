package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.School;

import java.util.List;
import java.util.Optional;

public interface SchoolDAO {
    Optional<School> getSchoolById(int id);
    Optional<School> getSchoolByName(String name);
    List<School> getAllSchools();

    void deleteSchoolById(int id);
    boolean deleteSchoolByName(String name);

    void addSchool(School school);
}
