package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.Report;
import com.swe.EduMeter.orm.ReportDAO;

import java.util.List;
import java.util.Optional;

public class PostgreReportDAO implements ReportDAO {

    @Override
    public Optional<Report> get(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Report> getAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int add(Report report) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(Report report) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
