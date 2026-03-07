package com.swe.EduMeter.orm.in_mem_dao;

import com.swe.EduMeter.models.PinChallenge;
import com.swe.EduMeter.orm.dao.PinChallengeDAO;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

public class InMemPinChallengeDAO implements PinChallengeDAO {
    private final Map<Integer, PinChallenge> store;
    private int id = 0;

    public InMemPinChallengeDAO(Map<Integer, PinChallenge> store) {
        this.store = store;
        setupIncrementalId();
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

    private void setupIncrementalId() {
        if (store.size() == 0) return;

        int maxKey = store.keySet()
                .stream()
                .max(Comparator.comparingInt(a -> a))
                .orElse(0);

        id = maxKey + 1;
    }
}
