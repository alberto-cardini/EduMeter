package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.orm.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Abstract class that handles executing queries and mapping results.

public abstract class PostgreDAO<T> {
    private void setParams(PreparedStatement stmt, List<Object> params) throws  SQLException {
        for (int i = 0; i < params.size(); i++) {
            stmt.setObject(i + 1, params.get(i));
        }
    }

    // Converts every row of the ResultSet into a concrete object.
    protected abstract T mapRowToObject(ResultSet rs) throws SQLException;


    // Executes a SELECT query and returns a list of mapped objects.
    //
    // Accepting a query String is safe here because this method is protected.
    // It is intended to be called only by concrete DAOs with internally defined queries,
    // never with direct user input.
    //
    // The connection is not closed as it's managed by the DatabaseManager.
    protected List<T> selectQuery(String query, List<Object> params) throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.closeOnCompletion();
        setParams(stmt, params);

        try (ResultSet rs = stmt.executeQuery()) {
            List<T> results = new ArrayList<>();
            while (rs.next()) {
                results.add(mapRowToObject(rs));
            }
            return results;
        }
    }

    protected List<T> selectQuery(String query) throws SQLException {
        return  selectQuery(query, List.of());
    }

    //Executes DELETE, UPDATE queries.
    protected void updateQuery(String query, List<Object> params) throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            setParams(stmt, params);

            stmt.executeUpdate();
        }
    }

    protected int insertQuery(String query, List<Object> params) throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.closeOnCompletion();
        setParams(stmt, params);

        try (ResultSet rs = stmt.executeQuery()) {
            if(rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
}