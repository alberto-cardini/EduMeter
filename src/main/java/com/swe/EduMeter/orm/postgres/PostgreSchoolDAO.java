package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.School;
import com.swe.EduMeter.orm.SchoolDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PostgreSchoolDAO extends PostgreDAO<School> implements SchoolDAO {
    @Override
    protected School mapRowToObject(ResultSet rs) throws SQLException {
        return new School(rs.getInt("id"), rs.getString("name"));
    }

    @Override
    public Optional<School> getSchoolById(int id) {
        String query = "SELECT * FROM School WHERE id = ?";
        List<Object> params = List.of(id);

        try {
            return runQuery(query, params).stream().findFirst();
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    @Override
    public Optional<School> getSchoolByName(String name) {
        String query = "SELECT * FROM School WHERE name = ?";
        List<Object> params = List.of(name);

        try {
            return runQuery(query, params).stream().findFirst();
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }

    }

    @Override
    public List<School> getAllSchools() {
        String query = "SELECT * FROM School";

        try {
            return runQuery(query);
        }catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    @Override
    public void addSchool(School school) {
        String query = "INSERT INTO School (name) VALUES (?)";
        List<Object> params = List.of(school.getName());

        try {
            runUpdate(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }

    }

    @Override
    public void deleteSchoolById(int id) {
        String query = "DELETE FROM School WHERE id = ?";
        List<Object> params = List.of(id);

        try {
            runUpdate(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    @Override
    public boolean deleteSchoolByName(String name) {
        String query = "DELETE FROM School WHERE name = ?";
        List<Object> params = List.of(name);

        try {
            runUpdate(query, params);
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }
}