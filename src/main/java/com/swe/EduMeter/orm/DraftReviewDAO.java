package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.DraftReview;

import java.util.List;
import java.util.Optional;

public interface DraftReviewDAO {
    Optional<DraftReview> get(int id);

    List<DraftReview> getAll();

    int add(DraftReview review);

    void update(DraftReview review);

    void delete(int id);
}
