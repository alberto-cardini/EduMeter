package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.Professor;
import com.swe.EduMeter.orm.ProfDAO;

import java.util.List;
import java.util.Optional;

public class PostgreProfDAO implements ProfDAO {

    @Override
    public Integer add(Professor prof) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Professor> get(int id){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(Professor prof) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Professor> search(String pattern) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
