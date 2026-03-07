package com.swe.EduMeter.orm.in_mem_dao;

import com.swe.EduMeter.models.User;
import com.swe.EduMeter.orm.dao.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InMemUserDAOTest {
    private UserDAO userDAO;
    private Map<String, User> userStore;

    @BeforeEach
    public void setup() {
        userStore = new HashMap<>(Map.of(
                "hash1", new User("hash1", false),
                "hash2", new User("hash2", true),
                "hash3", new User("hash3", false)
        ));
        userDAO = new InMemUserDAO(userStore);
    }

    @Test
    public void TestAddUser() {
        String newHash = "hash4";
        User newUser = new User(newHash, false);
        int expectedSize = userStore.size() + 1;

        userDAO.add(newUser);

        assertEquals(expectedSize, userStore.size());
        assertTrue(userStore.containsKey(newHash), "User should be added to the store");
        assertEquals(newUser, userStore.get(newHash));
    }

    @Test
    public void TestGetUser_Found() {
        String expectedHash = "hash1";

        Optional<User> user = userDAO.get(expectedHash);

        assertTrue(user.isPresent(), "User should be present");
        assertEquals(expectedHash, user.get().getHash());
        assertFalse(user.get().isBanned());
    }

    @Test
    public void TestGetUser_NotFound() {
        String invalidHash = "invalid_hash";

        Optional<User> user = userDAO.get(invalidHash);

        assertTrue(user.isEmpty(), "User should not be present");
    }

    @Test
    public void TestUpdateUser_Found() {
        User user = userStore.get("hash1");

        // Update the user's banned status
        user.setBanned(true);

        userDAO.update(user);

        Optional<User> updatedUser = userDAO.get(user.getHash());

        assertTrue(updatedUser.isPresent(), "User should be present");
        assertEquals(user.getHash(), updatedUser.get().getHash());
        assertTrue(updatedUser.get().isBanned(), "User banned status should be updated");
    }

    @Test
    public void TestUpdateUser_NotFound() {
        String invalidHash = "invalid_hash";
        User user = new User(invalidHash, true);

        userDAO.update(user);

        Optional<User> gotUser = userDAO.get(invalidHash);

        assertTrue(gotUser.isEmpty(), "User should not be present");
    }

    @Test
    public void TestSearchUser_All() {
        Boolean bannedQuery = null;

        List<User> gotUsers = userDAO.search(bannedQuery);

        assertEquals(userStore.size(), gotUsers.size());
    }

    @Test
    public void TestSearchUser_Banned() {
        Boolean bannedQuery = true;

        List<User> gotUsers = userDAO.search(bannedQuery);

        // Only "hash2" is banned
        assertEquals(1, gotUsers.size());
        assertEquals("hash2", gotUsers.get(0).getHash());
        assertTrue(gotUsers.get(0).isBanned());
    }

    @Test
    public void TestSearchUser_NotBanned() {
        Boolean bannedQuery = false;

        List<User> gotUsers = userDAO.search(bannedQuery);

        // "hash1" and "hash3" are not banned
        assertEquals(2, gotUsers.size());
        assertTrue(gotUsers.stream().noneMatch(User::isBanned));
    }
}