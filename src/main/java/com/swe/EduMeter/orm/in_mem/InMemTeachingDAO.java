package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.Teaching;
import com.swe.EduMeter.orm.TeachingDAO;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemTeachingDAO implements TeachingDAO {
    private final ConcurrentHashMap<Integer, Teaching> inMemTeachingStorage = new ConcurrentHashMap<>();
    private int id = 0;
    @Override
    public int add(Teaching teaching) {
        teaching.setId(id);
        inMemTeachingStorage.put(id, teaching);

        return id++;
    }

    @Override
    public Optional<Teaching> get(int id) {
        return Optional.ofNullable(inMemTeachingStorage.get(id));
    }

    @Override
    public void delete(int id) {
        inMemTeachingStorage.remove(id);
    }
}
