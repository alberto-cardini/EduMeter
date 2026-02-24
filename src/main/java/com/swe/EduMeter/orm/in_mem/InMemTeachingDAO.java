package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.PublishedReview;
import com.swe.EduMeter.model.Review;
import com.swe.EduMeter.model.Teaching;
import com.swe.EduMeter.orm.DAOFactory;
import com.swe.EduMeter.orm.PublishedReviewDAO;
import com.swe.EduMeter.orm.TeachingDAO;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemTeachingDAO implements TeachingDAO {
    private final ConcurrentHashMap<Integer, Teaching> inMemTeachingStorage = new ConcurrentHashMap<>();
    private int id = 0;
    @Override
    public int add(Teaching teaching) {
        DAOFactory factory = new InMemDAOFactory();

        // Validate ids
        factory.getProfDAO().get(teaching.getProfId()).orElseThrow(() -> new RuntimeException("Invalid profId"));
        factory.getCourseDAO().get(teaching.getCourseId()).orElseThrow(() -> new RuntimeException("Invalid courseId"));

        teaching.setId(id);
        inMemTeachingStorage.put(id, teaching);

        return id++;
    }

    @Override
    public Optional<Teaching> get(int id) {
        return Optional.ofNullable(inMemTeachingStorage.get(id));
    }

    @Override
    public void delete(int id) {
        Teaching t = inMemTeachingStorage.remove(id);

        if (t != null) {
            PublishedReviewDAO reviewDAO = new InMemDAOFactory().getPublishedReviewDAO();

            List<PublishedReview> reviews = reviewDAO.search(null, null,
                                                             t.getCourseId(), t.getProfId(),
                                                    null);

            for (Review r: reviews) {
                reviewDAO.delete(r.getId());
            }
        }
    }

    @Override
    public List<Teaching> getByCourse(int id) {
        return inMemTeachingStorage.values()
               .stream()
               .filter(t -> t.getCourseId().equals(id))
               .collect(Collectors.toList());
    }

    @Override
    public List<Teaching> getByProf(int id) {
        return inMemTeachingStorage.values()
                .stream()
                .filter(t -> t.getProfId().equals(id))
                .collect(Collectors.toList());
    }
}
