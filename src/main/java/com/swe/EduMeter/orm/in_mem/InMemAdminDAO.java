package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.Admin;
import com.swe.EduMeter.orm.AdminDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemAdminDAO implements AdminDAO {
    private final Map<Integer, Admin> store;
    private int id = 0;

    public InMemAdminDAO(Map<Integer, Admin> store) {
        this.store = store;
        //add(new Admin(null, "alberto.cardini@edu.unifi.it"));
        //add(new Admin(null, "lorenzo.bellina@edu.unifi.it"));
        //add(new Admin(null, "carolina.cecchi@edu.unifi.it"));
    }

    @Override
    public Optional<Admin> get(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Admin> getByEmail(String email) {
        return store.values()
                .stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findAny();
    }

    @Override
    public List<Admin> getAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public int add(Admin admin) {
        admin.setId(id);
        store.put(id, admin);

        return id++;
    }

    @Override
    public void delete(int id) {
        store.remove(id);
    }
}
