package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.School;

import java.util.List;
import java.util.Optional;

public interface SchoolDAO {
    int add(School school);
    Optional<School> get(int id);
    void update(School school);
    void delete(int id);

    /**
     * @param pattern The pattern to look for. A null
     *                pattern returns all schools.
     *
     * @return        List of schools
     */
    List<School> search(String pattern);
}
