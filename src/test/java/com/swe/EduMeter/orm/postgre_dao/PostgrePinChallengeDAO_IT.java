package com.swe.EduMeter.orm.postgre_dao;

import com.swe.EduMeter.models.PinChallenge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostgrePinChallengeDAO_IT extends PostgreIT {
    private final PostgrePinChallengeDAO pinChallengeDAO = new PostgrePinChallengeDAO();

    public static void insertPinChallenges() throws SQLException {
        Connection c = DatabaseManager.getInstance().getConnection();
        String statement = "INSERT INTO Pin_Challenge (pin, user_id, expires_at, is_admin) VALUES" +
                "    ('1234', 'user000000000000000001', '2024-03-15 10:30:00', false)," +
                "    ('0000', 'user000000000000000002', '2024-03-15 11:00:00', false)," +
                "    ('7777', 'user000000000000000003', '2024-03-15 12:00:00', true);";

        try (PreparedStatement st = c.prepareStatement(statement)) {
            st.execute();
        }
    }

    @BeforeEach
    public void setup() throws SQLException {
        PostgreUserDAO_IT.insertUsers();
        insertPinChallenges();
    }

    @Test
    public void testGet_Found() {
        int validId = 1;

        Optional<PinChallenge> result = pinChallengeDAO.get(validId);

        assertTrue(result.isPresent(), "PinChallenge should be present");
        assertEquals("1234", result.get().getPin());
    }

    @Test
    public void testGet_NotFound() {
        int invalidId = 999;

        Optional<PinChallenge> result = pinChallengeDAO.get(invalidId);

        assertTrue(result.isEmpty(), "PinChallenge should not be present");
    }

    @Test
    public void testAdd() {
        String pin = "4321";
        PinChallenge newChallenge = new PinChallenge(null, pin, "user000000000000000001", Instant.now(), false);

        int generatedId = pinChallengeDAO.add(newChallenge);
        Optional<PinChallenge> insertedPinChallenge = pinChallengeDAO.get(generatedId);

        assertTrue(insertedPinChallenge.isPresent(), "Pin challenge should be inserted");
        assertEquals(pin, insertedPinChallenge.get().getPin());
    }

    @Test
    public void testDelete_Found() {
        int validId = 1;

        pinChallengeDAO.delete(validId);
        Optional<PinChallenge> deletedPinChallenge = pinChallengeDAO.get(validId);

        assertTrue(deletedPinChallenge.isEmpty(), "Pin challenge should be deleted");
    }

    @Test
    public void testDelete_NotFound() {
        int invalidId = 999;

        assertDoesNotThrow(() -> pinChallengeDAO.delete(invalidId));
    }
}
