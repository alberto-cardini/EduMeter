package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.Admin;
import com.swe.EduMeter.orm.AdminDAO;

import java.util.List;
import java.util.Optional;

public class PostgreAdminDAO implements AdminDAO
{

    @Override
    public Optional<Admin> get(int id)
    {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Optional<Admin> getByEmail(String email)
    {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public List<Admin> getAll()
    {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public int add(Admin admin)
    {
        //TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void delete(int id)
    {
        // TODO
        throw new RuntimeException("Not implemented");
    }

}