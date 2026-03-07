package com.swe.EduMeter.orm.in_mem_dao;

import com.swe.EduMeter.models.PinChallenge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InMemPinChallengeDAOTest {

    private InMemPinChallengeDAO pinChallengeDAO;
    private Map<Integer, PinChallenge> store;

    @BeforeEach
    public void setup() {
        store = new HashMap<>();

        PinChallenge pc0 = new PinChallenge(0, "1234", "USER1",
                                            Instant.now().plus(Duration.ofMinutes(30)), true);

        PinChallenge pc1 = new PinChallenge(1, "0000", "USER2",
                                            Instant.now().plus(Duration.ofMinutes(30)), false);

        store.put(0, pc0);
        store.put(1, pc1);

        pinChallengeDAO = new InMemPinChallengeDAO(store);
    }

    @Test
    public void testGet_Found() {
        Optional<PinChallenge> result = pinChallengeDAO.get(0);

        assertTrue(result.isPresent(), "PinChallenge should be present");
        assertEquals(0, result.get().getId());
    }

    @Test
    public void testGet_NotFound() {
        Optional<PinChallenge> result = pinChallengeDAO.get(999);

        assertTrue(result.isEmpty(), "PinChallenge should not be found for invalid ID");
    }

    @Test
    public void testAdd() {
        PinChallenge newChallenge = new PinChallenge();

        int generatedId = pinChallengeDAO.add(newChallenge);

        assertEquals(store.size() - 1, generatedId);
    }

    @Test
    public void testDelete() {
        int prevSize = store.size();
        pinChallengeDAO.delete(0);

        assertFalse(store.containsKey(0), "PinChallenge should be removed from the store");
        assertEquals(prevSize - 1, store.size(), "Store size should be decremented");
    }
}