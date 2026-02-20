package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.Review;
import com.swe.EduMeter.model.ReviewStatus;
import java.util.List;
import java.util.Optional;

public interface ReviewDAO {
    Optional<Review> get(int id, ReviewStatus status);

    List<Review> list(ReviewStatus status);

    List<Review> search(Integer school_id, Integer degree_id, Integer course_id, Integer professor_id);

    int add(Review review);

    void update(Review review);

    void updateStatus(int id, ReviewStatus status);

    void incrementVote(int id);

    void delete(int id);
}