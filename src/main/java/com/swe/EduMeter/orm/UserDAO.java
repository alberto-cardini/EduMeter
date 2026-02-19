package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    void add(User user);
    Optional<User> get(String hash);
    void update(User user);

    /**
     * @param banned When != null, searches for all
     *               banned or unbanned users.
     * @return       The list of users, optionally filtered
     */
    List<User> search(Boolean banned);
}
