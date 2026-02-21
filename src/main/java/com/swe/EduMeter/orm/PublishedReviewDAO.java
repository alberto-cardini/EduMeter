package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.PublishedReview;
import java.util.List;
import java.util.Optional;

public interface PublishedReviewDAO {
    Optional<PublishedReview> get(int id);

    List<PublishedReview> search(Integer schoolId, Integer degreeId, Integer courseId, Integer professorId);

    int add(PublishedReview review);

    void update(PublishedReview review);

    void toggleUpVote(int id, String userHash);

    void delete(int id);
}