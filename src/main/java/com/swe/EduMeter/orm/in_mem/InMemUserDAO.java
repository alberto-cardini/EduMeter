package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.User;
import com.swe.EduMeter.orm.UserDAO;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemUserDAO implements UserDAO {
    private final ConcurrentHashMap<Integer, User> inMemStorage = new ConcurrentHashMap<>();
    private int id = 0;

    public InMemUserDAO() {
        add(new User(0, "PROVA1", false));
        add(new User(0, "PROVA2", false));
        add(new User(0, "PROVA3", false));
    }

    @Override
    public Optional<User> getById(int id) {
        return Optional.ofNullable(inMemStorage.get(id));
    }

    @Override
    public Optional<User> getByHash(String hash) {
        return inMemStorage.values()
                .stream()
                .filter(u -> u.getHash().equals(hash))
                .findAny();
    }

    @Override
    public void add(User user) {
        user.setId(id);
        inMemStorage.put(id, user);
        id++;
    }

    @Override
    public List<User> search(Boolean banned) {
        return inMemStorage.values().stream()
                .filter(u -> banned == null || u.isBanned().equals(banned))
                .toList();
    }
}
