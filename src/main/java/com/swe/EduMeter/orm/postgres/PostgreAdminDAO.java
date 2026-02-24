package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.Admin;
import com.swe.EduMeter.orm.AdminDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PostgreAdminDAO extends PostgreDAO<Admin> implements AdminDAO {

    @Override
    protected Admin mapRowToObject(ResultSet rs) throws SQLException {
        return new Admin(rs.getInt("id"), rs.getString("email"));
    }

    @Override
    public Optional<Admin> get(int id) {
        String query = "SELECT * FROM Admin WHERE id = ?";
        List<Object> params = List.of(id);

        try {
            return selectQuery(query, params).stream().findFirst();
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Admin> getByEmail(String email) {
        String query = "SELECT * FROM Admin WHERE email = ?";
        List<Object> params = List.of(email);

        try {
            return selectQuery(query, params).stream().findFirst();
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Admin> getAll() {
        String query = "SELECT * FROM Admin";

        try {
            return selectQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public int add(Admin admin) {
        String query = "INSERT INTO Admin (email) VALUES (?)";
        List<Object> params = List.of(admin.getEmail());

        try {
            return insertQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM Admin WHERE id = ?";
        List<Object> params = List.of(id);

        try {
            updateQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

}