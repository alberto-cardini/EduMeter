package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.Teaching;
import com.swe.EduMeter.orm.TeachingDAO;

import java.util.Optional;

public class PostgreTeachingDAO implements TeachingDAO {
    @Override
    public int add(Teaching teaching) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public Optional<Teaching> get(int id) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public void delete(int id) {
        throw new RuntimeException("Not implemented!");
    }
}
