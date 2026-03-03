package com.swe.EduMeter.orm.postgre_dao;

import com.swe.EduMeter.models.DraftReview;
import com.swe.EduMeter.orm.dao.DraftReviewDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PostgreDraftReviewDAO extends PostgreDAO<DraftReview> implements DraftReviewDAO {
    @Override
    protected DraftReview mapRowToObject(ResultSet rs) throws SQLException {
        DraftReview review = new DraftReview();
        review.setId(rs.getInt("id"));
        review.setCreatorHash(rs.getString("user_id"));
        review.setDate(rs.getDate("date").toLocalDate());
        review.setRawSchool(rs.getString("school"));
        review.setRawDegree(rs.getString("degree"));
        review.setRawCourse(rs.getString("course"));
        review.setRawProfessor(rs.getString("professor"));
        review.setEnjoyment(rs.getInt("enjoyment"));
        review.setDifficulty(rs.getInt("difficulty"));
        review.setComment(rs.getString("comment"));

        return review;
    }

    @Override
    public Optional<DraftReview> get(int id) {
        String query = "SELECT * FROM DraftReview WHERE id = ?";
        List<Object> params = List.of(id);

        return selectQuery(query, params).stream().findFirst();
    }

    @Override
    public List<DraftReview> getAll() {
        String query = "SELECT * FROM DraftReview";

        return selectQuery(query);
    }

    @Override
    public int add(DraftReview review) {
        String query = """
                INSERT INTO Drafted_Review
                (user_id, date, school, degree, course, professor, enjoyment, difficulty, comment)
                VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        List<Object> params = List.of(
                review.getCreatorHash(), review.getDate(),
                review.getRawSchool(), review.getRawDegree(),
                review.getRawCourse(), review.getRawProfessor(),
                review.getEnjoyment(), review.getDifficulty(),
                review.getComment()
        );

        return insertQuery(query, params);
    }

    @Override
    public void update(DraftReview review) {
        String query = """ 
                UPDATE Drafted_Review SET user_id=?, date=?, school=?, degree=?,
                course=?, professor=?, enjoyment=?, difficulty=?, comment=? WHERE id=?
                """;
        List<Object> params = List.of(
                review.getCreatorHash(), review.getDate(),
                review.getRawSchool(), review.getRawDegree(),
                review.getRawCourse(), review.getRawProfessor(),
                review.getEnjoyment(), review.getDifficulty(),
                review.getComment(), review.getId()
        );

        updateQuery(query, params);
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM DraftReview WHERE id = ?";
        List<Object> params = List.of(id);

        updateQuery(query, params);
    }
}