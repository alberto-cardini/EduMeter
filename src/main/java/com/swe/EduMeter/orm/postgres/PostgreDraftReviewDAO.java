package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.DraftReview;
import com.swe.EduMeter.orm.DraftReviewDAO;

import java.util.List;
import java.util.Optional;

public class PostgreDraftReviewDAO implements DraftReviewDAO {
    @Override
    public Optional<DraftReview> get(int id) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public List<DraftReview> getAll() {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public int add(DraftReview review) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public void update(DraftReview review) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public void delete(int id) {
        throw new RuntimeException("Not implemented!");
    }
}
