package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.Pin;
import com.swe.EduMeter.orm.PinDAO;

import java.util.Comparator;
import java.util.Optional;
import java.util.Vector;

public class InMemPinDAO implements PinDAO {
    private final Vector<Pin> inMemPinStorage = new Vector<>();
    @Override
    public Optional<Pin> get(String userHash, boolean isAdmin) {
        return inMemPinStorage.stream()
                              .filter(p -> p.getUserHash().equals(userHash) && p.isAdmin() == isAdmin)
                              .max(Comparator.comparing(Pin::getExpiresAt));
    }

    @Override
    public void add(Pin pin) {
        inMemPinStorage.add(pin);
    }

    @Override
    public void delete(int id) {
        for (Pin p: inMemPinStorage) {
            if (p.getId().equals(id)) {
                inMemPinStorage.remove(p);
                return;
            }
        }
    }
}
