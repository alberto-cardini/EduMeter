package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.User;
import com.swe.EduMeter.orm.UserDAO;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemUserDAO implements UserDAO {
    private final ConcurrentHashMap<String, User> inMemStorage = new ConcurrentHashMap<>();

    public InMemUserDAO() {
        add(new User("PROVA1", false));
        add(new User("PROVA2", false));
        add(new User("PROVA3", false));
        add(new User("email", false));
    }

    @Override
    public void add(User user) {
        inMemStorage.put(user.getHash(), user);
    }

    @Override
    public Optional<User> get(String hash) {
        return Optional.ofNullable(inMemStorage.get(hash));
    }

    @Override
    public void update(User user) {
        inMemStorage.replace(user.getHash(), user);
    }

    @Override
    public List<User> search(Boolean banned) {
        return inMemStorage.values().stream()
                .filter(u -> banned == null || u.isBanned().equals(banned))
                .toList();
    }
}
