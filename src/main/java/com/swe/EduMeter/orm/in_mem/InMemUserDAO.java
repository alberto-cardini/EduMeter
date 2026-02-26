package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.User;
import com.swe.EduMeter.orm.UserDAO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemUserDAO implements UserDAO {
    private final Map<String, User> store;

    public InMemUserDAO(Map<String, User> store) {
        this.store = store;
        //add(new User("PROVA1", false));
        //add(new User("PROVA2", false));
        //add(new User("PROVA3", false));
        //add(new User("email", false));
    }

    @Override
    public void add(User user) {
        store.put(user.getHash(), user);
    }

    @Override
    public Optional<User> get(String hash) {
        return Optional.ofNullable(store.get(hash));
    }

    @Override
    public void update(User user) {
        store.replace(user.getHash(), user);
    }

    @Override
    public List<User> search(Boolean banned) {
        return store.values().stream()
                .filter(u -> banned == null || u.isBanned().equals(banned))
                .toList();
    }
}
