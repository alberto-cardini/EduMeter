package com.swe.EduMeter.orm.in_mem;

import com.swe.EduMeter.model.User;
import com.swe.EduMeter.orm.UserDAO;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemUserDAO implements UserDAO {
    private final ConcurrentHashMap<Integer, User> inMemStorage = new ConcurrentHashMap<>();
    private int id = 0;

    public InMemUserDAO() {
        addUser(new User(0, "PROVA1", false));
        addUser(new User(0, "PROVA2", false));
        addUser(new User(0, "PROVA3", false));
    }

    @Override
    public Optional<User> getUserById(int id) {
        return Optional.ofNullable(inMemStorage.get(id));
    }

    @Override
    public Optional<User> getUserByHash(String hash) {
        return inMemStorage.values()
                .stream()
                .filter(u -> u.getHash().equals(hash))
                .findAny();
    }

    @Override
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(inMemStorage.values());
    }

    @Override
    public ArrayList<User> getAllBannedUsers() {
        return inMemStorage.values().stream()
                .filter(User::isBanned) // Keep only banned users
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void addUser(User user) {
        user.setId(id);
        inMemStorage.put(id, user);
        id++;
    }
}
