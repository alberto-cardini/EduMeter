package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.Report;
import com.swe.EduMeter.orm.PublishedReviewDAO;
import com.swe.EduMeter.orm.ReportDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemReportDAO implements ReportDAO {
    private final Map<Integer, Report> store;
    private final PublishedReviewDAO publishedReviewDAO;
    private int id = 0;

    public InMemReportDAO(Map<Integer, Report> store, PublishedReviewDAO publishedReviewDAO) {
        this.store = store;
        this.publishedReviewDAO = publishedReviewDAO;
    }

    @Override
    public Optional<Report> get(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Report> getAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public int add(Report report) {
        publishedReviewDAO
            .get(report.getReviewId(), null)
            .orElseThrow(() -> new RuntimeException("Invalid reportId"));

        report.setId(id);
        store.put(id, report);

        return id++;
    }

    @Override
    public void update(Report report) {
        store.replace(report.getId(), report);
    }

    @Override
    public void delete(int id) {
        store.remove(id);
    }
}
