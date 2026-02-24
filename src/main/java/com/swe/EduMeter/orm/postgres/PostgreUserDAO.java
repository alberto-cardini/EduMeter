package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.User;
import com.swe.EduMeter.orm.UserDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PostgreUserDAO extends PostgreDAO<User> implements UserDAO {
    @Override
    protected User mapRowToObject(ResultSet rs) throws SQLException {
        return new User(rs.getString("id"), rs.getBoolean("banned"));
    }

    @Override
    public void add(User user) {
        String query = "INSERT INTO Users (id, banned) VALUES (?, ?)";
        List<Object> params = List.of(user.getHash(), Boolean.FALSE);

        try {
            insertQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> get(String hash) {
        String query = "SELECT * FROM Users WHERE id = ?";
        List<Object> params = List.of(hash);

        try {
            return selectQuery(query, params).stream().findFirst();
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(User user) {
        String query = "UPDATE Users SET (banned) VALUES (?)";
        List<Object> params = List.of(user.isBanned());

        try {
            updateQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<User> search(Boolean banned) {
        String query = "SELECT * FROM Users WHERE banned = ?";
        List<Object> params = List.of(banned);

        try {
            return selectQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

}
