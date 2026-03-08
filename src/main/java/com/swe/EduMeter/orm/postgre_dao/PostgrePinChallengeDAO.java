package com.swe.EduMeter.orm.postgre_dao;

import com.swe.EduMeter.models.PinChallenge;
import com.swe.EduMeter.orm.dao.PinChallengeDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class PostgrePinChallengeDAO extends PostgreDAO<PinChallenge> implements PinChallengeDAO {

    @Override
    protected PinChallenge mapRowToObject(ResultSet rs) throws SQLException {
        PinChallenge pin = new PinChallenge();
        pin.setId(rs.getInt("id"));
        pin.setPin(rs.getString("pin"));
        pin.setUserHash(rs.getString("user_id"));
        pin.setExpiresAt(rs.getTimestamp("expires_at").toInstant());
        pin.setAdmin(rs.getBoolean("is_admin"));

        return pin;
    }

    @Override
    public Optional<PinChallenge> get(Integer id) {
        String query = "SELECT * FROM Pin_Challenge WHERE id = ?";
        List<Object> params = List.of(id);

        return selectQuery(query, params).stream().findFirst();
    }

    @Override
    public int add(PinChallenge pin) {
        String query = "INSERT INTO Pin_Challenge(pin, user_id, expires_at, is_admin) VALUES (?, ?, ?, ?)";
        List<Object> params = List.of(
                pin.getPin(), pin.getUserHash(),
                Timestamp.from(pin.getExpiresAt()), pin.isAdmin()
        );

        return (Integer) insertQuery(query, params);
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM Pin_Challenge WHERE id = ?;";
        List<Object> params = List.of(id);

        updateQuery(query, params);
    }
}
