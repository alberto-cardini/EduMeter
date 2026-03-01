package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.Admin;
import com.swe.EduMeter.model.response.ApiObjectCreated;
import com.swe.EduMeter.model.response.ApiOk;
import com.swe.EduMeter.orm.AdminDAO;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Mock
    private AdminDAO adminDAO;

    @InjectMocks
    private AdminController adminController;

    @Test
    public void testGetAll() {
        List<Admin> admins = List.of(
                new Admin(1, "admin1@edumeter.com"),
                new Admin(2, "admin2@edumeter.com")
        );
        when(adminDAO.getAll()).thenReturn(admins);

        List<Admin> gotAdmins = adminController.getAll();

        assertEquals(admins.size(), gotAdmins.size());
        assertEquals(admins.get(0).getEmail(), gotAdmins.get(0).getEmail());
        verify(adminDAO, times(1)).getAll();
    }

    @Test
    public void testGet_Found() {
        int id = 42;
        Admin admin = new Admin(id, "admin@edumeter.com");
        when(adminDAO.get(id)).thenReturn(Optional.of(admin));

        Admin gotAdmin = adminController.get(id);

        assertEquals(admin, gotAdmin);
    }

    @Test
    public void testGet_NotFound() {
        int id = 42;
        when(adminDAO.get(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> adminController.get(id));

        assertEquals("Admin not found", exception.getMessage());
    }

    @Test
    public void testCreate() {
        Admin admin = new Admin(null, "new_admin@edumeter.com");
        int expectedId = 99;

        when(adminDAO.add(admin)).thenReturn(expectedId);

        ApiObjectCreated response = adminController.create(admin);

        assertEquals(expectedId, response.id());
        assertEquals("Admin created", response.message());
        verify(adminDAO, times(1)).add(admin);
    }

    @Test
    public void testDelete_Found() {
        int id = 42;
        Admin admin = new Admin(id, "admin@edumeter.com");
        // delete() internally calls this.get(id), so we must mock the get() method to return an Admin
        when(adminDAO.get(id)).thenReturn(Optional.of(admin));

        ApiOk response = adminController.delete(id);

        assertEquals("Admin deleted", response.message());
        verify(adminDAO, times(1)).delete(id);
    }

    @Test
    public void testDelete_NotFound() {
        int id = 42;
        when(adminDAO.get(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> adminController.delete(id));

        assertEquals("Admin not found", exception.getMessage());
        // Verify delete is never reached if get() throws an exception
        verify(adminDAO, never()).delete(anyInt());
    }
}