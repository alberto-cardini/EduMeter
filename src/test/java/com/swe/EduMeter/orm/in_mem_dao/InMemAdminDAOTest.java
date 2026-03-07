package com.swe.EduMeter.orm.in_mem_dao;

import com.swe.EduMeter.models.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InMemAdminDAOTest {

    private InMemAdminDAO adminDAO;
    private Map<Integer, Admin> store;

    @BeforeEach
    public void setup() {
        store = new HashMap<>();
        store.put(0, new Admin(0, "alberto.cardini@edu.unifi.it"));
        store.put(1, new Admin(1, "lorenzo.bellina@edu.unifi.it"));

        adminDAO = new InMemAdminDAO(store);
    }

    @Test
    public void testGet_Found() {
        int targetId = 0;

        Optional<Admin> result = adminDAO.get(targetId);

        assertTrue(result.isPresent());
        assertEquals(store.get(targetId).getEmail(), result.get().getEmail());
    }

    @Test
    public void testGet_NotFound() {
        int nonExistingId = store.size();

        Optional<Admin> result = adminDAO.get(nonExistingId);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetByEmail_CaseInsensitive() {
        int targetId = 1;
        String email = store.get(targetId).getEmail().toUpperCase();

        Optional<Admin> result = adminDAO.getByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(targetId, result.get().getId());
    }

    @Test
    public void testGetByEmail_NotFound() {
        Optional<Admin> result = adminDAO.getByEmail("nonexistent@edu.unifi.it");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAll() {
        List<Admin> allAdmins = adminDAO.getAll();

        assertEquals(store.size(), allAdmins.size());
    }

    @Test
    public void testAdd() {
        Admin newAdmin = new Admin(null, "carolina.cecchi@edu.unifi.it");

        int generatedId = adminDAO.add(newAdmin);

        assertEquals("carolina.cecchi@edu.unifi.it", store.get(generatedId).getEmail());
        assertEquals(generatedId, store.get(generatedId).getId());
        assertEquals(generatedId, store.size() - 1);
    }

    @Test
    public void testDelete_Found() {
        int existingId = 0;

        adminDAO.delete(existingId);

        assertFalse(store.containsKey(0));
        assertEquals(1, store.size());
    }

    @Test
    public void testDelete_NotFound() {
        int prevSize = store.size();
        int nonExistingId = prevSize;

        adminDAO.delete(nonExistingId);

        assertEquals(prevSize, store.size());
    }
}