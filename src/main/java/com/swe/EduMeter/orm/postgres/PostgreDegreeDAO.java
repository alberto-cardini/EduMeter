package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.Degree;
import com.swe.EduMeter.orm.DegreeDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgreDegreeDAO extends PostgreDAO<Degree> implements DegreeDAO {

    @Override
    protected Degree mapRowToObject(ResultSet rs) throws SQLException {
        Degree degree = new Degree();
        degree.setId(rs.getInt("id"));
        degree.setName(rs.getString("name"));
        degree.setType(Degree.Type.valueOf(rs.getString("type")));
        degree.setSchoolId(rs.getInt("school_id"));

        return degree;
    }

    @Override
    public int add(Degree degree) {
        String query = "INSERT INTO Degree (name, type, school_id) VALUES (?, ?, ?)";
        List<Object> params = List.of(degree.getName(), degree.getType().toString(), degree.getSchoolId());

        try {
            return insertQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Degree> get(int id) {
        String query = "SELECT * FROM Degree WHERE id = ?";
        List<Object> params = List.of(id);

        try {
            return selectQuery(query, params).stream().findFirst();
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Degree degree) {
        String query = "UPDATE Degree SET name = ?, type = ?, school_id = ?  WHERE id = ?";
        List<Object> params = List.of(degree.getName(), degree.getType().toString(), degree.getSchoolId(), degree.getId());

        try {
            updateQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM Degree WHERE id = ?";
        List<Object> params = List.of(id);

        try {
            updateQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Degree> search(String pattern, Integer schoolId) {
        StringBuilder query = new StringBuilder("SELECT * FROM Degree WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (pattern != null) {
            query.append(" AND LOWER(name) LIKE ?");
            params.add("%" + pattern.trim().toLowerCase() + "%");
        }

        if (schoolId != null) {
            query.append(" AND school_id = ?");
            params.add(schoolId);
        }

        try {
            String finalQuery = query.toString();
            return selectQuery(finalQuery, params);

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }
}