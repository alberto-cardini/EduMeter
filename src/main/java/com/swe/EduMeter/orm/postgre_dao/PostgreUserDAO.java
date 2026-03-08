package com.swe.EduMeter.orm.postgre_dao;

import com.swe.EduMeter.models.User;
import com.swe.EduMeter.orm.dao.UserDAO;

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

        insertQuery(query, params);
    }

    @Override
    public Optional<User> get(String hash) {
        String query = "SELECT * FROM Users WHERE id = ?";
        List<Object> params = List.of(hash);

        return selectQuery(query, params).stream().findFirst();
    }

    @Override
    public void update(User user) {
        String query = "UPDATE Users SET banned = ? WHERE id = ?";
        List<Object> params = List.of(user.isBanned(), user.getHash());

        updateQuery(query, params);
    }

    @Override
    public List<User> search(Boolean banned) {
        String query = "SELECT * FROM Users";
        List<Object> params = List.of();

        if (banned != null) {
            query += " WHERE banned = ?";
            params = List.of(banned);
        }

        return selectQuery(query, params);
    }
}
