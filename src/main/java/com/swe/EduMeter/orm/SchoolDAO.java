package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.School;

import java.util.Optional;

public interface SchoolDAO {
    Optional<School> getSchoolById(int id);
    Optional<School> getSchoolByName(String name);
    void deleteSchoolById(int id);
    void deleteSchoolByName(String name);
    void addSchool(School school);
}
