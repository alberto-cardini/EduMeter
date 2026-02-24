package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.PinChallenge;
import com.swe.EduMeter.orm.PinChallengeDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PostgrePinChallengeDAO extends PostgreDAO<PinChallenge> implements PinChallengeDAO {

    @Override
    protected PinChallenge mapRowToObject(ResultSet rs) throws SQLException {
        PinChallenge pin = new PinChallenge();
        pin.setId(rs.getInt("id"));
        pin.setPin(rs.getString("pin"));
        pin.setUserHash(rs.getString("user_hash"));
        pin.setExpiresAt(rs.getTimestamp("expire_at").toInstant());
        pin.setAdmin(rs.getBoolean("admin"));

        return pin;
    }

    @Override
    public Optional<PinChallenge> get(Integer id) {
        String query = "SELECT * FROM Pin WHERE id = ?";
        List<Object> params = List.of(id);

        try {
            return selectQuery(query, params).stream().findFirst();
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public int add(PinChallenge pin) {
        String query = "INSERT INTO Pin VALUES (?, ?, ?, ?, ?)";
        List<Object> params = List.of(
                pin.getPin(), pin.getUserHash(),
                pin.getExpiresAt(), pin.isAdmin()
        );

        try {
            return insertQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM Pin WHERE id = ?;";
        List<Object> params = List.of(id);

        try {
            updateQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }
}
