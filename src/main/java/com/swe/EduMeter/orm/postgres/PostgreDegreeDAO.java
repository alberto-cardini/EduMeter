package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.Degree;
import com.swe.EduMeter.model.School;
import com.swe.EduMeter.orm.DegreeDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PostgreDegreeDAO extends PostgreDAO<Degree> implements DegreeDAO {

    @Override
    protected Degree mapRowToObject(ResultSet rs) throws SQLException {
        Degree degree = new Degree();
        degree.setId(rs.getInt("id"));
        degree.setName(rs.getString("name"));
        degree.setType(Degree.Type.valueOf(rs.getString("type")));

        School school = new School(rs.getInt("school_id"), rs.getString("school_name"));
        degree.setSchool(school);

        return degree;
    }

    @Override
    public int add(Degree degree) {
        String query = "INSERT INTO Degree (name, type, school_id) VALUES (?, ?, ?) RETURNING id";
        List<Object> params = List.of(degree.getName(), degree.getType().toString(), degree.getSchool().getId());

        try {
            return insertQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    @Override
    public Optional<Degree> get(int id) {
        String query = "SELECT d.id, d.name, d.type, d.school_id, s.name AS school_name " +
                "FROM Degree d JOIN School s ON d.school_id = s.id " +
                "WHERE d.id = ?";
        List<Object> params = List.of(id);

        try {
            return selectQuery(query, params).stream().findFirst();
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    @Override
    public void update(Degree degree) {
        String query = "UPDATE Degree SET name = ?, type = ?, school_id = ?  WHERE id = ?";
        List<Object> params = List.of(degree.getName(), degree.getType().toString(), degree.getSchool().getId(), degree.getId());

        try {
            updateQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM Degree WHERE id = ?";
        List<Object> params = List.of(id);

        try {
            updateQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    @Override
    public List<Degree> search(String pattern, Integer schoolId) {
        try {
            // filter by pattern
            if (schoolId == null) {
                if (pattern == null) {
                    String query = "SELECT d.id, d.name, d.type, d.school_id, s.name AS school_name " +
                            "FROM Degree d JOIN School s ON d.school_id = s.id";

                    return selectQuery(query);
                } else {
                    String query = "SELECT d.id, d.name, d.type, d.school_id, s.name AS school_name " +
                            "FROM Degree d JOIN School s ON d.school_id = s.id " +
                            "WHERE LOWER(d.name) LIKE ?";
                    List<Object> params = List.of("%" + pattern.toLowerCase() + "%");

                    return selectQuery(query, params);
                }
            }
            // filter by school_id
            else {
                if (pattern == null) {
                    String query = """ 
                            SELECT d.id, d.name, d.type, d.school_id, s.name AS school_name
                            FROM Degree d JOIN School s ON d.school_id = s.id
                            WHERE s.id = ?
                            """;
                    List<Object> params = List.of(schoolId);

                    return selectQuery(query, params);
                } else {
                    String query = """ 
                            SELECT d.id, d.name, d.type, d.school_id, s.name AS school_name
                            FROM Degree d JOIN School s ON d.school_id = s.id
                            WHERE s.id = ? AND LOWER(d.name) LIKE ?
                            """;
                    List<Object> params = List.of(schoolId, "%" + pattern.toLowerCase() + "%");

                    return selectQuery(query, params);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }
}
