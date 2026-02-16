package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.Admin;
import com.swe.EduMeter.orm.AdminDAO;

import java.util.ArrayList;
import java.util.Optional;

public class PostgreAdminDAO implements AdminDAO {

    @Override
    public Optional<Admin> getAdminById(int id) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Optional<Admin> getAdminByEmail(String email) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ArrayList<Admin> getAllAdmins() {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean addAdmin(Admin admin) {
        //TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void deleteAdminById(int id) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

}