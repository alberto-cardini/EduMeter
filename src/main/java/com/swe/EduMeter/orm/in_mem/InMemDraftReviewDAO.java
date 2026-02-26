package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.DraftReview;
import com.swe.EduMeter.orm.DraftReviewDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemDraftReviewDAO implements DraftReviewDAO {
    private final Map<Integer, DraftReview> store;
    private int id = 0;

    public InMemDraftReviewDAO(Map<Integer, DraftReview> store) {
        this.store = store;
    }
    @Override
    public Optional<DraftReview> get(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<DraftReview> getAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public int add(DraftReview review) {
        review.setId(id);
        store.put(id, review);

        return id++;
    }

    @Override
    public void update(DraftReview review) {
        store.put(review.getId(), review);
    }

    @Override
    public void delete(int id) {
        store.remove(id);
    }
}
