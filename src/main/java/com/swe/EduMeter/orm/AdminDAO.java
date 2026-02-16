package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.Admin;

import java.util.ArrayList;
import java.util.Optional;

public interface AdminDAO {
    Optional<Admin> getAdminById(int id);
    Optional<Admin> getAdminByEmail(String email);
    ArrayList<Admin> getAllAdmins();

    void deleteAdminById(int id);

    boolean addAdmin(Admin admin);
}
