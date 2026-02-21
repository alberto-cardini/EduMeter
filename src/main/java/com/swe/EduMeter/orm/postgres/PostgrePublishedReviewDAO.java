package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.PublishedReview;
import com.swe.EduMeter.orm.PublishedReviewDAO;

import java.util.List;
import java.util.Optional;

public class PostgrePublishedReviewDAO implements PublishedReviewDAO {
    @Override
    public Optional<PublishedReview> get(int id) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public List<PublishedReview> search(Integer schoolId, Integer degreeId, Integer courseId, Integer professorId) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public int add(PublishedReview review) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public void update(PublishedReview review) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public void toggleUpVote(int id, String userHash) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public void delete(int id) {
        throw new RuntimeException("Not implemented!");
    }
}
