package com.swe.EduMeter.orm.postgre_dao;

import com.swe.EduMeter.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PostgreUserDAO_IT extends PostgreIT {
    private final PostgreUserDAO userDAO = new PostgreUserDAO();

    public static void insertUsers() throws SQLException {
        Connection c = DatabaseManager.getInstance().getConnection();
        String statement = "INSERT INTO Users (id, banned) VALUES" +
                "('user000000000000000001', false)," +
                "('user000000000000000002', false)," +
                "('user000000000000000003', true);";

        try (PreparedStatement st = c.prepareStatement(statement)) {
            st.execute();
        }
    }

    @BeforeEach
    public void setup() throws SQLException {
        insertUsers();
    }

    @Test
    public void testAdd() {
        String newHash = "user000000000000000004";
        User newUser = new User(newHash, false);

        userDAO.add(newUser);
        Optional<User> insertedUser = userDAO.get(newHash);

        assertTrue(insertedUser.isPresent(), "User should be inserted");
        assertEquals(newHash, insertedUser.get().getHash());
        assertFalse(insertedUser.get().isBanned());
    }

    @Test
    public void testGet_Found() {
        String expectedHash = "user000000000000000001";

        Optional<User> user = userDAO.get(expectedHash);

        assertTrue(user.isPresent(), "User should be present");
        assertEquals(expectedHash, user.get().getHash());
        assertFalse(user.get().isBanned());
    }

    @Test
    public void testGet_NotFound() {
        String invalidHash = "invalid_hash";

        Optional<User> user = userDAO.get(invalidHash);

        assertTrue(user.isEmpty(), "User should not be present");
    }

    @Test
    public void testUpdate_Found() {
        User user = userDAO.get("user000000000000000001")
                .orElseThrow(() -> new RuntimeException("User should be present"));
        user.setBanned(true);

        userDAO.update(user);
        Optional<User> updatedUser = userDAO.get(user.getHash());

        assertTrue(updatedUser.isPresent(), "User should be present");
        assertEquals(user.getHash(), updatedUser.get().getHash());
        assertTrue(updatedUser.get().isBanned(), "User banned status should be updated");
    }

    @Test
    public void testUpdate_NotFound() {
        String invalidHash = "invalid_hash";
        User user = new User(invalidHash, true);

        userDAO.update(user);

        Optional<User> gotUser = userDAO.get(invalidHash);

        assertTrue(gotUser.isEmpty(), "User should not be present");
    }

    @Test
    public void testSearch_All() {
        Boolean bannedQuery = null;

        List<User> gotUsers = userDAO.search(bannedQuery);

        assertEquals(3, gotUsers.size());
    }

    @Test
    public void TestSearchUser_Banned() {
        Boolean bannedQuery = true;

        List<User> gotUsers = userDAO.search(bannedQuery);

        // Only "user0000000000000000003" is banned
        assertEquals(1, gotUsers.size());
        assertEquals("user000000000000000003", gotUsers.get(0).getHash());
        assertTrue(gotUsers.get(0).isBanned());
    }

    @Test
    public void TestSearchUser_NotBanned() {
        Boolean bannedQuery = false;

        List<User> gotUsers = userDAO.search(bannedQuery);

        // "user000000000000000001" and "user000000000000000002" are not banned
        assertEquals(2, gotUsers.size());
        assertTrue(gotUsers.stream().noneMatch(User::isBanned));
    }

}
