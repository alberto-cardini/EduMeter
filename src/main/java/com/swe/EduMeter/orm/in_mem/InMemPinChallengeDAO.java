package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.PinChallenge;
import com.swe.EduMeter.orm.PinChallengeDAO;

import java.util.Map;
import java.util.Optional;

public class InMemPinChallengeDAO implements PinChallengeDAO {
    private final Map<Integer, PinChallenge> store;
    private int id = 0;

    public InMemPinChallengeDAO(Map<Integer, PinChallenge> store) {
        this.store = store;
    }
    @Override
    public Optional<PinChallenge> get(Integer id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public int add(PinChallenge pinChallenge) {
        pinChallenge.setId(id);
        store.put(id, pinChallenge);

        return id++;
    }

    @Override
    public void delete(int id) {
        store.remove(id);
    }
}
