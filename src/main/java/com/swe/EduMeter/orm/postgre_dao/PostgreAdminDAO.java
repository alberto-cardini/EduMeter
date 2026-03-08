package com.swe.EduMeter.orm.postgre_dao;

import com.swe.EduMeter.models.Admin;
import com.swe.EduMeter.orm.dao.AdminDAO;

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

        return selectQuery(query, params).stream().findFirst();
    }

    @Override
    public Optional<Admin> getByEmail(String email) {
        String query = "SELECT * FROM Admin WHERE LOWER(email) = ?";
        List<Object> params = List.of(email.toLowerCase());

        return selectQuery(query, params).stream().findFirst();
    }

    @Override
    public List<Admin> getAll() {
        String query = "SELECT * FROM Admin";

        return selectQuery(query);
    }

    @Override
    public int add(Admin admin) {
        String query = "INSERT INTO Admin (email) VALUES (?)";
        List<Object> params = List.of(admin.getEmail());

        return (Integer) insertQuery(query, params);
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM Admin WHERE id = ?";
        List<Object> params = List.of(id);

        updateQuery(query, params);
    }

}