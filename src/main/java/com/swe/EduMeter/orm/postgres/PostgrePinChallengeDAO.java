package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.PinChallenge;
import com.swe.EduMeter.orm.PinChallengeDAO;

import java.util.Optional;

public class PostgrePinChallengeDAO implements PinChallengeDAO {
    @Override
    public Optional<PinChallenge> get(Integer id) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public int add(PinChallenge pinChallenge) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public void delete(int id) {
        throw new RuntimeException("Not implemented!");
    }
}
