package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.PinChallenge;

import java.util.Optional;

public interface PinChallengeDAO {
    Optional<PinChallenge> get(Integer id);
    int add(PinChallenge pinChallenge);

    /**
     * After Pin authentication, remove the Pin challenge,
     * to free up storage. This does not prevent storage
     * cluttering as unsuccessful attempts to login will
     * still take up space.
     *
     * @param id The Pin challenge to delete.
     */
    void delete(int id);
}
