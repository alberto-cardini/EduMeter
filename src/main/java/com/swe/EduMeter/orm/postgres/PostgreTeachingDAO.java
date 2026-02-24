package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.Teaching;
import com.swe.EduMeter.orm.TeachingDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PostgreTeachingDAO extends PostgreDAO<Teaching> implements TeachingDAO {
    @Override
    protected Teaching mapRowToObject(ResultSet rs) throws SQLException {
        return new Teaching(rs.getInt("id"), rs.getInt("course_id"), rs.getInt("professor_id"));
    }

    @Override
    public int add(Teaching teaching) {
        String query = "INSERT INTO Teaching (course_id, professor_id) VALUES (?, ?)";
        List<Object> params = List.of(teaching.getCourseId(), teaching.getProfId());

        try {
            return insertQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Teaching> get(int id) {
        String query = "SELECT * FROM Teaching WHERE id = ?";
        List<Object> params = List.of(id);

        try {
            return selectQuery(query, params).stream().findFirst();
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM Teaching WHERE id = ?";
        List<Object> params = List.of(id);

        try {
            updateQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Teaching> getByCourse(int id) {
        String query = "SELECT * FROM Teaching WHERE course_id = ?";
        List<Object> params = List.of(id);

        try {
            return selectQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Teaching> getByProf(int id) {
        String query = "SELECT * FROM Teaching WHERE professor_id = ?";
        List<Object> params = List.of(id);

        try {
            return selectQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }
}
