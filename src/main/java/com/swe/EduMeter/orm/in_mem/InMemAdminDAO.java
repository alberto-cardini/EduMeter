package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.Admin;
import com.swe.EduMeter.orm.AdminDAO;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemAdminDAO implements AdminDAO {
    private final ConcurrentHashMap<Integer, Admin> inMemStorage = new ConcurrentHashMap<>();
    private int id = 0;

    public InMemAdminDAO() {
        addAdmin(new Admin(0, "alberto.cardini@edu.unifi.it"));
        addAdmin(new Admin(0, "lorenzo.bellina@edu.unifi.it"));
        addAdmin(new Admin(0, "carolina.cecchi@edu.unifi.it"));
    }

    @Override
    public Optional<Admin> getAdminById(int id) {
        return Optional.ofNullable(inMemStorage.get(id));
    }

    @Override
    public Optional<Admin> getAdminByEmail(String email) {
        return inMemStorage.values()
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findAny();
    }

    @Override
    public ArrayList<Admin> getAllAdmins() {
        return new ArrayList<>(inMemStorage.values());
    }

    // TODO: idk who should have the privileges to use this methods and have the ability to create admins.
    @Override
    public boolean addAdmin(Admin admin) {
        if(!getAdminByEmail(admin.getEmail()).isPresent()) {
            admin.setId(id);
            inMemStorage.put(id, admin);
            id++;
            return true;
        }
        return false;
    }

    @Override
    public void deleteAdminById(int id) { inMemStorage.remove(id); }

}
