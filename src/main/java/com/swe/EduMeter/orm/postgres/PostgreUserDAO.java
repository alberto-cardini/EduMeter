package com.swe.EduMeter.orm.postgres;

import com.swe.EduMeter.model.User;
import com.swe.EduMeter.orm.UserDAO;
import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;
import java.util.Optional;

public class PostgreUserDAO implements UserDAO {
    @Override
    public Optional<User> getUserById(int id) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Optional<User> getUserByHash(String hash) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ArrayList<User> getAllUsers() {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ArrayList<User> getAllBannedUsers() {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void addUser(User user) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ArrayList<User> getUsersFilteredForBan(boolean banned) {
        // TODO
        throw new RuntimeException("Not implemented");
    }
}
