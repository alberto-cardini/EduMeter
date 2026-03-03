package com.swe.EduMeter.orm.postgre_dao;

import com.swe.EduMeter.models.School;
import com.swe.EduMeter.orm.dao.SchoolDAO;

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
    public int add(School school) {
        String query = "INSERT INTO School (name) VALUES (?)";
        List<Object> params = List.of(school.getName());

        return insertQuery(query, params);
    }

    @Override
    public Optional<School> get(int id) {
        String query = "SELECT * FROM School WHERE id = ?";
        List<Object> params = List.of(id);

        return selectQuery(query, params).stream().findFirst();
    }

    @Override
    public void update(School school) {
        String query = "UPDATE School SET name = ? WHERE id = ?";
        List<Object> params = List.of(school.getName(), school.getId());

        updateQuery(query, params);
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM School WHERE id = ?";
        List<Object> params = List.of(id);

        updateQuery(query, params);
    }

    @Override
    public List<School> search(String pattern) {
        if (pattern == null) {
            String query = "SELECT * FROM School";
            return selectQuery(query);
        } else {
            String query = "SELECT * FROM School WHERE LOWER(name) LIKE ?";
            List<Object> params = List.of("%" + pattern.toLowerCase() + "%");

            return selectQuery(query, params);
        }
    }
}