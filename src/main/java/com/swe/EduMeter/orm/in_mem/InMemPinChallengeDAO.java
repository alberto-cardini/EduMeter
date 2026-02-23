package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.PinChallenge;
import com.swe.EduMeter.orm.PinChallengeDAO;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemPinChallengeDAO implements PinChallengeDAO {
    private final ConcurrentHashMap<Integer, PinChallenge> inMemPinStorage = new ConcurrentHashMap<>();
    private int id = 0;
    @Override
    public Optional<PinChallenge> get(Integer id) {
        return Optional.ofNullable(inMemPinStorage.get(id));
    }

    @Override
    public int add(PinChallenge pinChallenge) {
        pinChallenge.setId(id);
        inMemPinStorage.put(id, pinChallenge);

        return id++;
    }

    @Override
    public void delete(int id) {
        inMemPinStorage.remove(id);
    }
}
