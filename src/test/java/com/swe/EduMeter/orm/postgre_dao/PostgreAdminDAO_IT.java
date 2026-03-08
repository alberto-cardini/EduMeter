package com.swe.EduMeter.orm.postgre_dao;

import com.swe.EduMeter.models.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostgreAdminDAO_IT extends PostgreIT {
    private final PostgreAdminDAO adminDAO = new PostgreAdminDAO();

    public static void insertAdmins() throws SQLException {
        Connection c = DatabaseManager.getInstance().getConnection();
        String statement = "INSERT INTO Admin (email) VALUES" +
                "('foo@bar.com')," +
                "('baz@bar.com')";

        try (PreparedStatement st = c.prepareStatement(statement)) {
            st.execute();
        }
    }

    @BeforeEach
    public void setup() throws SQLException {
        insertAdmins();
    }

    @Test
    public void testGet_Found() {
        int validId = 1;

        Optional<Admin> result = adminDAO.get(validId);

        assertTrue(result.isPresent());
        assertEquals("foo@bar.com", result.get().getEmail());
    }

    @Test
    public void testGet_NotFound() {
        int invalidId = 999;

        Optional<Admin> result = adminDAO.get(invalidId);

        assertTrue(result.isEmpty(), "Admin should not be present");
    }

    @Test
    public void testGetByEmail_Found() {
        int targetId = 1;
        String email = "FOO@BAR.com";

        Optional<Admin> gotAdmin = adminDAO.getByEmail(email);

        assertTrue(gotAdmin.isPresent());
        assertEquals(targetId, gotAdmin.get().getId());
    }

    @Test
    public void testGetByEmail_NotFound() {
        Optional<Admin> result = adminDAO.getByEmail("nonexistent@edu.unifi.it");

        assertTrue(result.isEmpty(), "Admin should not be present");
    }

    @Test
    public void testGetAll() {
        List<Admin> allAdmins = adminDAO.getAll();

        assertEquals(2, allAdmins.size());
    }

    @Test
    public void testAdd() {
        String newEmail = "foo@baz.com";
        Admin newAdmin = new Admin(null, newEmail);

        int generatedId = adminDAO.add(newAdmin);
        Optional<Admin> insertedAdmin = adminDAO.get(generatedId);

        assertTrue(insertedAdmin.isPresent(), "Admin should be inserted");
        assertEquals(newEmail, insertedAdmin.get().getEmail());
    }

    @Test
    public void testDelete_Found() {
        int validId = 1;

        adminDAO.delete(validId);
        Optional<Admin> deletedAdmin = adminDAO.get(validId);

        assertTrue(deletedAdmin.isEmpty(), "Admin should be deleted");
    }

    @Test
    public void testDelete_NotFound() {
        int invalidId = 999;

        assertDoesNotThrow(() -> adminDAO.delete(invalidId));
    }
}
