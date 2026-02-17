package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.Degree;

import java.util.List;
import java.util.Optional;

public interface DegreeDAO {
    int add(Degree degree);
    Optional<Degree> get(int id);
    void update(Degree degree);
    void delete(int id);

    /**
     * @param pattern  The pattern to look for. A null
     *                 does not apply a name filter.
     * @param schoolId The school to filter for. A null
     *                 schoolId looks for degrees of
     *                 every school.
     *
     * @return        List of degrees
     */
    List<Degree> search(String pattern, Integer schoolId);
}
