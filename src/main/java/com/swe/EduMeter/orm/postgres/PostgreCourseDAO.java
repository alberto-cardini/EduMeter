package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.Course;
import com.swe.EduMeter.model.Degree;
import com.swe.EduMeter.orm.CourseDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgreCourseDAO extends PostgreDAO<Course> implements CourseDAO {

    @Override
    protected Course mapRowToObject(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setId(rs.getInt("id"));
        course.setName(rs.getString("name"));
        course.setDegree(rs.getInt("degree_id"));

        return course;
    }

    @Override
    public Integer add(Course course) {
        String query = "INSERT INTO Course (name, degree_id) VALUES (?, ?)";
        List<Object> params = List.of(course.getName(), course.getDegreeId());

        try {
            return insertQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Course> get(int id) {
        String query = "SELECT * FROM course WHERE id = ?";
        List<Object> params = List.of(id);

        try {
            return selectQuery(query, params).stream().findFirst();
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Course course) {
        String query = "UPDATE Course SET name = ?, degree_id = ?  WHERE id = ?";
        List<Object> params = List.of(course.getName(), course.getDegreeId(), course.getId());

        try {
            updateQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM Course WHERE id = ?";
        List<Object> params = List.of(id);

        try {
            updateQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Course> search(String pattern, Integer schoolId, Integer degreeId) {
        StringBuilder query = new StringBuilder("SELECT * FROM Course c");
        if (schoolId != null && degreeId == null) {
            query.append(" JOIN Degree d ON d.id = degree_id");
        }
        query.append(" WHERE 1=1");

        List<Object> params = new ArrayList<>();

        if (degreeId != null) {
            query.append(" AND c.degree_id = ?");
            params.add(degreeId);
        }
        else{
            if (schoolId != null) {
                query.append(" AND d.school_id = ?");
                params.add(schoolId);
            }
        }

        if (pattern != null) {
            query.append(" AND LOWER(c.name) LIKE ?");
            params.add("%" + pattern.toLowerCase() + "%");
        }

        try {
            String finalQuery = query.toString();

            return selectQuery(finalQuery, params);

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }
}