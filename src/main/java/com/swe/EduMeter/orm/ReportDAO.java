package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.Report;

import java.util.List;
import java.util.Optional;

public interface ReportDAO
{
    Optional<Report> get(int id);
    List<Report> getAll();
    int create(Report report);
    boolean update(Report report);
    boolean delete(Report report);
}
