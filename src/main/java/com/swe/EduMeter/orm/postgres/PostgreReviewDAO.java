package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.Review;
import com.swe.EduMeter.model.ReviewStatus;
import com.swe.EduMeter.orm.ReviewDAO;

import java.util.List;
import java.util.Optional;

public class PostgreReviewDAO implements ReviewDAO {

    @Override
    public Optional<Review> get(int id, ReviewStatus status) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Review> list(ReviewStatus status) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Review> search(Integer school_id, Integer degree_id, Integer course_id, Integer professor_id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int add(Review review) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean update(Review review) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean updateStatus(int id, ReviewStatus status) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean incrementVote(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean delete(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
