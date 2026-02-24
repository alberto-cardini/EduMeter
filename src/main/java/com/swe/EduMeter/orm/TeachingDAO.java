package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.Teaching;

import java.util.List;
import java.util.Optional;

public interface TeachingDAO {
    int add(Teaching teaching);
    Optional<Teaching> get(int id);
    List<Teaching> getByCourse(int courseId);
    List<Teaching> getByProf(int profId);
    void delete(int id);
}
