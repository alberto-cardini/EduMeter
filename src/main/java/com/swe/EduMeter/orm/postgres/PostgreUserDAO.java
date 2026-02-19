package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.User;
import com.swe.EduMeter.orm.UserDAO;

import java.util.List;
import java.util.Optional;

public class PostgreUserDAO implements UserDAO {
    @Override
    public void add(User user) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Optional<User> get(String hash) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void update(User user) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public List<User> search(Boolean banned) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

}
