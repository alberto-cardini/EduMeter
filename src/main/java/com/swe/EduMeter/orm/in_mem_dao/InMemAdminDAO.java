package com.swe.EduMeter.orm.in_mem_dao;

import com.swe.EduMeter.models.Admin;
import com.swe.EduMeter.orm.dao.AdminDAO;

import java.util.*;

public class InMemAdminDAO implements AdminDAO {
    private final Map<Integer, Admin> store;
    private int id = 0;

    public InMemAdminDAO(Map<Integer, Admin> store) {
        this.store = store;
        setupIncrementalId();
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

    private void setupIncrementalId() {
        if (store.size() == 0) return;

        int maxKey = store.keySet()
                .stream()
                .max(Comparator.comparingInt(a -> a))
                .orElse(0);

        id = maxKey + 1;
    }
}
