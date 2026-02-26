package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.PublishedReview;
import com.swe.EduMeter.orm.PublishedReviewDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgrePublishedReviewDAO extends PostgreDAO<PublishedReview> implements PublishedReviewDAO {
    @Override
    protected PublishedReview mapRowToObject(ResultSet rs) throws SQLException {
        PublishedReview review = new PublishedReview();
        review.setId(rs.getInt("id"));
        review.setCreatorHash(rs.getString("user_id"));
        review.setDate(rs.getDate("date").toLocalDate());
        review.setComment(rs.getString("comment"));
        review.setEnjoyment(rs.getInt("enjoyment"));
        review.setDifficulty(rs.getInt("difficulty"));
        review.setTeachingId(rs.getInt("teaching_id"));

        return review;
    }

    @Override
    public Optional<PublishedReview> get(int id, String userHash) {
        String query = """
                SELECT pr.*,
                    (SELECT count(*) FROM Up_vote WHERE review_id = pr.id) AS upvotes_count,
                    EXISTS (SELECT 1 FROM Up_vote WHERE review_id = pr.id AND user_id = ?) AS is_upvoted
                FROM Published_Review pr
                WHERE pr.id = ?;
                """;
        List<Object> params = List.of(userHash, id);

        try (ResultSet rs = rawQuery(query, params)) {
            if (rs.next()) {
                PublishedReview review = mapRowToObject(rs);
                review.setUpvotes(rs.getInt("upvotes_count"));
                review.setIsUpvotedByUser(rs.getBoolean("is_upvoted"));

                return Optional.of(review);
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public int add(PublishedReview review) {
        String query = """
        INSERT INTO Published_Review (user_id, date, teaching_id, enjoyment, difficulty, comment)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

        List<Object> params = List.of(
            review.getCreatorHash(), review.getDate(),
            review.getTeachingId(), review.getEnjoyment(),
            review.getDifficulty(), review.getComment()
        );

        try {
            return insertQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(PublishedReview review) {
        String query = """
                UPDATE Published_Review SET user_id=?, date=?, teaching_id=?,
                enjoyment=?, difficulty=?, comment=? WHERE id=?
                """;

        List<Object> params = List.of(
            review.getCreatorHash(), review.getDate(),
            review.getTeachingId(), review.getEnjoyment(),
            review.getDifficulty(), review.getComment(),
            review.getId()
        );
        try {
            updateQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public void toggleUpvote(int id, String userHash) {
        String query = """
                WITH deleted AS (DELETE FROM Up_vote WHERE review_id = ? AND user_id = ? RETURNING *)
                
                INSERT INTO Up_vote (review_id, user_id) SELECT ?, ?
                WHERE NOT EXISTS (SELECT 1 FROM deleted)
                """;

        List<Object> params = List.of(id, userHash, id, userHash);

        try {
            updateQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM Published_Review WHERE id = ?";
        List<Object> params = List.of(id);

        try {
            updateQuery(query, params);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PublishedReview> search(Integer schoolId, Integer degreeId, Integer courseId, Integer professorId, String userHash) {
        StringBuilder query = new StringBuilder("""
                SELECT pr.*,
                (SELECT count(*) FROM Up_vote WHERE review_id = r.id) AS upvotes_count
                EXISTS (SELECT 1 FROM Up_vote WHERE review_id = r.id AND user_id = ?) AS is_upvoted
                FROM Published_Review pr
                """);
        List<Object> params = new ArrayList<>();
        params.add(userHash);

        if (courseId != null || professorId != null || schoolId != null || degreeId != null) {
            query.append(" JOIN Teaching t ON t.id = teaching_id");
        }
        if (degreeId != null || schoolId != null) {
            query.append(" JOIN Course c ON c.id = t.course_id");
        }
        if (schoolId != null) {
            query.append(" JOIN Degree d ON d.id = c.degree_id");
        }
        query.append(" WHERE 1=1");

        if (professorId != null) {
            query.append(" AND t.professor_id = ?");
            params.add(professorId);
        }
        if (courseId != null) {
            query.append(" AND t.course_id = ?");
            params.add(courseId);
        } else if (degreeId != null) {
            query.append(" AND c.degree_id = ?");
            params.add(degreeId);
        } else if (schoolId != null) {
            query.append(" AND d.school_id = ?");
            params.add(schoolId);
        }

        try (ResultSet rs = rawQuery(query.toString(), params)) {
            List<PublishedReview> results = new ArrayList<>();

            while (rs.next()) {
                PublishedReview review = mapRowToObject(rs);
                review.setUpvotes(rs.getInt("upvotes_count"));
                review.setIsUpvotedByUser(rs.getBoolean("is_upvoted"));

                results.add(review);
            }
            return results;

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }

    }
}
