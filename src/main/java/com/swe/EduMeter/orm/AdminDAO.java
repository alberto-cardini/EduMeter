package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.Admin;

import java.util.List;
import java.util.Optional;

public interface AdminDAO
{
    Optional<Admin> get(int id);
    Optional<Admin> getByEmail(String email);
    List<Admin> getAll();
    void delete(int id);
    boolean add(Admin admin);
}