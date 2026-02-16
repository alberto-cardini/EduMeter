package com.swe.EduMeter.orm;

import com.swe.EduMeter.model.User;

import java.util.ArrayList;
import java.util.Optional;

public interface UserDAO {
    Optional<User> getUserById(int id);
    Optional<User> getUserByHash(String hash);
    ArrayList<User> getAllUsers();
    ArrayList<User> getAllBannedUsers();

    ArrayList<User> getUsersFilteredForBan(boolean banned);

    //boolean deleteUserById(int id);

    void addUser(User user);
}
