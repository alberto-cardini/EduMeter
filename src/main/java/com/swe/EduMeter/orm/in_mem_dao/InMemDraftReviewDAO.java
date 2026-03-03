package com.swe.EduMeter.orm.in_mem_dao;

import com.swe.EduMeter.model.DraftReview;
import com.swe.EduMeter.orm.dao.DraftReviewDAO;

import java.util.*;

public class InMemDraftReviewDAO implements DraftReviewDAO {
    private final Map<Integer, DraftReview> store;
    private int id = 0;

    public InMemDraftReviewDAO(Map<Integer, DraftReview> store) {
        this.store = store;
        setupIncrementalId();
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
        store.replace(review.getId(), review);
    }

    @Override
    public void delete(int id) {
        store.remove(id);
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
