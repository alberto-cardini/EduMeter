package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.Admin;
import com.swe.EduMeter.orm.AdminDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemAdminDAO implements AdminDAO {
    private final ConcurrentHashMap<Integer, Admin> inMemStorage = new ConcurrentHashMap<>();
    private int id = 0;

    public InMemAdminDAO() {
        add(new Admin(null, "alberto.cardini@edu.unifi.it"));
        add(new Admin(null, "lorenzo.bellina@edu.unifi.it"));
        add(new Admin(null, "carolina.cecchi@edu.unifi.it"));
    }

    @Override
    public Optional<Admin> get(int id) {
        return Optional.ofNullable(inMemStorage.get(id));
    }

    @Override
    public Optional<Admin> getByEmail(String email) {
        return inMemStorage.values()
                .stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findAny();
    }

    @Override
    public List<Admin> getAll() {
        return new ArrayList<>(inMemStorage.values());
    }

    @Override
    public int add(Admin admin) {
        admin.setId(id);
        inMemStorage.put(id, admin);

        return id++;
    }

    @Override
    public void delete(int id) {
        inMemStorage.remove(id);
    }
}
