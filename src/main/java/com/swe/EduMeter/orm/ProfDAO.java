package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.Professor;

import java.util.List;
import java.util.Optional;

public interface ProfDAO {
    int add(Professor prof);
    Optional<Professor> get(int id);
    void update(Professor prof);
    void delete(int id);

    /**
     * @param pattern  The pattern to look for. A null
     *                 does not apply a name filter.
     * @param courseId Filter for professors teaching a
     *                 certain course. A null courseId does
     *                 not apply the filter.
     * @return         List of professors
     */
    List<Professor> search(String pattern, Integer courseId);
}
