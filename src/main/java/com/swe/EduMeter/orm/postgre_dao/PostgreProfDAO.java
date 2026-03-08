package com.swe.EduMeter.orm.postgre_dao;

import com.swe.EduMeter.models.Professor;
import com.swe.EduMeter.orm.dao.ProfDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgreProfDAO extends PostgreDAO<Professor> implements ProfDAO {

    @Override
    protected Professor mapRowToObject(ResultSet rs) throws SQLException {
        Professor prof = new Professor();
        prof.setId(rs.getInt("id"));
        prof.setName(rs.getString("name"));
        prof.setSurname(rs.getString("surname"));

        return prof;
    }

    @Override
    public int add(Professor prof) {
        String query = "INSERT INTO Professor (name, surname) VALUES (?, ?)";
        List<Object> params = List.of(prof.getName(), prof.getSurname());

        return (Integer) insertQuery(query, params);
    }

    @Override
    public Optional<Professor> get(int id) {
        String query = "SELECT * FROM Professor WHERE id = ?";
        List<Object> params = List.of(id);

        return selectQuery(query, params).stream().findFirst();
    }

    @Override
    public void update(Professor prof) {
        String query = "UPDATE Professor SET name = ?, surname = ? WHERE id = ?";
        List<Object> params = List.of(prof.getName(), prof.getSurname(), prof.getId());

        updateQuery(query, params);
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM Professor WHERE id = ?";
        List<Object> params = List.of(id);

        updateQuery(query, params);
    }

    @Override
    public List<Professor> search(String pattern, Integer courseId) {
        StringBuilder query = new StringBuilder("SELECT p.* FROM Professor p");
        List<Object> params = new ArrayList<>();

        if (courseId != null) {
            query.append(" JOIN Teaching t ON p.id = professor_id");
        }
        query.append(" WHERE 1=1");

        if (courseId != null) {
            query.append(" AND t.course_id = ?");
            params.add(courseId);
        }

        if (pattern != null) {
            String[] words = pattern.toLowerCase().split("\\s+");

            for (String word : words) {
                query.append(" AND (LOWER(name) LIKE ? OR LOWER(surname) LIKE ?)");

                params.add("%" + word + "%");
                params.add("%" + word + "%");
            }
        }

        String finalQuery = query.toString();
        return selectQuery(finalQuery, params);
    }

}