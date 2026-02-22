package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.DraftReview;
import com.swe.EduMeter.orm.DraftReviewDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemDraftReviewDAO implements DraftReviewDAO {
    private final ConcurrentHashMap<Integer, DraftReview> inMemDraftReviewStorage = new ConcurrentHashMap<>();
    private int id = 0;
    @Override
    public Optional<DraftReview> get(int id) {
        return Optional.ofNullable(inMemDraftReviewStorage.get(id));
    }

    @Override
    public List<DraftReview> getAll() {
        return new ArrayList<>(inMemDraftReviewStorage.values());
    }

    @Override
    public int add(DraftReview review) {
        review.setId(id);
        inMemDraftReviewStorage.put(id, review);

        return id++;
    }

    @Override
    public void update(DraftReview review) {
        inMemDraftReviewStorage.put(review.getId(), review);
    }

    @Override
    public void delete(int id) {
        inMemDraftReviewStorage.remove(id);
    }
}
