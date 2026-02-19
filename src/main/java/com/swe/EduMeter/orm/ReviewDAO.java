package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.Review;
import com.swe.EduMeter.model.ReviewStatus;
import java.util.List;
import java.util.Optional;

public interface ReviewDAO {
    Optional<Review> get(int id);

    // Recupera le recensioni PENDING (dalla tabella Drafted)
    List<Review> getAllPending();

    // Ricerca tra le recensioni ACCEPTED (dalla tabella Published)
    List<Review> search(Integer school_id, Integer degree_id, Integer course_id, Integer professor_id);

    int create(Review review);

    // Usato dall'admin per "trasferire" una recensione da Drafted a Published
    boolean update(Review review);

    boolean updateStatus(int id, ReviewStatus status);

    boolean incrementVote(int id);

    boolean delete(int id);
}