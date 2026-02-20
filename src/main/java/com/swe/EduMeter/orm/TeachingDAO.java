package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.Teaching;

public interface TeachingDAO {
    int add(Teaching teaching);
    Teaching get(int id);
    void delete(int id);
}
