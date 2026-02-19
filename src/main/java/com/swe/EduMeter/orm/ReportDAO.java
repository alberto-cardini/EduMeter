package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.Report;

import java.util.List;
import java.util.Optional;

public interface ReportDAO {
    Optional<Report> get(int id);
    List<Report> getAll();
    int add(Report report);
    void update(Report report);
    void delete(int id);
}
