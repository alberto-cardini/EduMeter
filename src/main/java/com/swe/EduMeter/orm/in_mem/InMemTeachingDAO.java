package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.Teaching;
import com.swe.EduMeter.orm.TeachingDAO;

public class InMemTeachingDAO implements TeachingDAO {
    @Override
    public int add(Teaching teaching) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public Teaching get(int id) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public void delete(int id) {
        throw new RuntimeException("Not implemented!");
    }
}
