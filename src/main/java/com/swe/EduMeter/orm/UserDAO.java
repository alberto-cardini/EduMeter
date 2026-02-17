package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Optional<User> getById(int id);
    Optional<User> getByHash(String hash);
    void add(User user);
    List<User> search(Boolean banned);
}
