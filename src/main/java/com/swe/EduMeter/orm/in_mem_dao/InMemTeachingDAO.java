package com.swe.EduMeter.orm.in_mem_dao;

import com.swe.EduMeter.models.PublishedReview;
import com.swe.EduMeter.models.Review;
import com.swe.EduMeter.models.Teaching;
import com.swe.EduMeter.orm.dao.CourseDAO;
import com.swe.EduMeter.orm.dao.ProfDAO;
import com.swe.EduMeter.orm.dao.PublishedReviewDAO;
import com.swe.EduMeter.orm.dao.TeachingDAO;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemTeachingDAO implements TeachingDAO {
    private final Map<Integer, Teaching> store;
    private final ProfDAO profDAO;
    private final CourseDAO courseDAO;
    private final PublishedReviewDAO reviewDAO;
    private int id = 0;

    public InMemTeachingDAO(Map<Integer, Teaching> store, CourseDAO courseDAO,
                            ProfDAO profDAO, PublishedReviewDAO reviewDAO) {
        this.store = store;
        this.courseDAO = courseDAO;
        this.profDAO = profDAO;
        this.reviewDAO = reviewDAO;
        setupIncrementalId();
    }

    @Override
    public int add(Teaching teaching) {
        // Validate ids
        profDAO.get(teaching.getProfId()).orElseThrow(() -> new RuntimeException("Invalid profId"));
        courseDAO.get(teaching.getCourseId()).orElseThrow(() -> new RuntimeException("Invalid courseId"));

        teaching.setId(id);
        store.put(id, teaching);

        return id++;
    }

    @Override
    public Optional<Teaching> get(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void delete(int id) {
        Teaching t = store.remove(id);

        if (t != null) {
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
        return store.values()
               .stream()
               .filter(t -> t.getCourseId().equals(id))
               .collect(Collectors.toList());
    }

    @Override
    public List<Teaching> getByProf(int id) {
        return store.values()
                .stream()
                .filter(t -> t.getProfId().equals(id))
                .collect(Collectors.toList());
    }

    private void setupIncrementalId() {
        if (store.size() == 0) return;

        int maxKey = store.keySet()
                .stream()
                .max(Comparator.comparingInt(a -> a))
                .orElse(0);

        id = maxKey + 1;
    }
}
