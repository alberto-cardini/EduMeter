package com.swe.EduMeter.orm.postgre_dao;

import com.swe.EduMeter.models.Report;
import com.swe.EduMeter.orm.dao.ReportDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PostgreReportDAO extends PostgreDAO<Report> implements ReportDAO {

    @Override
    protected Report mapRowToObject(ResultSet rs) throws SQLException {
        Report report = new Report();
        report.setId(rs.getInt("id"));
        report.setComment(rs.getString("comment"));
        report.setDate(rs.getDate("date").toLocalDate());
        report.setIssuerHash(rs.getString("issuer_id"));
        report.setReviewId(rs.getInt("review_id"));

        return report;
    }

    @Override
    public Optional<Report> get(int id) {
        String query = "SELECT * FROM Report WHERE id = ?";
        List<Object> params = List.of(id);

        return selectQuery(query, params).stream().findFirst();
    }

    @Override
    public List<Report> getAll() {
        String query = "SELECT * FROM Report";

        return selectQuery(query);
    }

    @Override
    public int add(Report report) {
        String query = "INSERT INTO Report (comment, date, user_id, review_id) VALUES(?, ?, ?, ?, ?)";
        List<Object> params = List.of(
                report.getComment(), report.getDate(),
                report.getIssuerHash(), report.getReviewId()
        );

        return insertQuery(query, params);
    }

    @Override
    public void update(Report report) {
        String query = "UPDATE Report SET date=?, user_id=?, review_id=?, comment=? WHERE id=?";
        List<Object> params = List.of(
                report.getDate(), report.getIssuerHash(),
                report.getReviewId(), report.getComment(),
                report.getId()
        );

        updateQuery(query, params);
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM Report WHERE id=?";
        List<Object> params = List.of(id);

        updateQuery(query, params);
    }
}