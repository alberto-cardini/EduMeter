package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.Pin;
import com.swe.EduMeter.orm.PinDAO;

import java.util.Optional;

public class InMemPinDAO implements PinDAO {
    @Override
    public Optional<Pin> get(String userHash, boolean isAdmin) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public void add(Pin pin) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public void delete(int id) {
        throw new RuntimeException("Not implemented!");
    }
}
