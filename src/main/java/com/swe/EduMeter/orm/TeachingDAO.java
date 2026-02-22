package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.Teaching;

import java.util.Optional;

public interface TeachingDAO {
    int add(Teaching teaching);
    Optional<Teaching> get(int id);
    void delete(int id);
}
