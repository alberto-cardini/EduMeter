package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.Report;
import com.swe.EduMeter.orm.ReportDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemReportDAO implements ReportDAO {
    private final ConcurrentHashMap<Integer, Report> inMemReportStorage = new ConcurrentHashMap<>();
    private int id = 0;

    public InMemReportDAO() {}

    @Override
    public Optional<Report> get(int id) {
        return Optional.ofNullable(inMemReportStorage.get(id));
    }

    @Override
    public List<Report> getAll() {
        return new ArrayList<>(inMemReportStorage.values());
    }

    @Override
    public int add(Report report) {
        report.setId(id);
        inMemReportStorage.put(id, report);

        return id++;
    }

    @Override
    public void update(Report report) {
        inMemReportStorage.replace(report.getId(), report);
    }

    @Override
    public void delete(int id) {
        inMemReportStorage.remove(id);
    }
}
