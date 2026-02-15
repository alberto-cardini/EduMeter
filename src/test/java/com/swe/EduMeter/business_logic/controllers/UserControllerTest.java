package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.User;
import com.swe.EduMeter.orm.UserDAO;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserDAO userDAO;
    @InjectMocks
    private UserController userController;

    @Test
    public void testGetUser_Found() {
        String userHash = "ABCDEF";
        User mockUser = new User(0, userHash, false);
        when(userDAO.getUserByHash(userHash)).thenReturn(Optional.of(mockUser));

        Response response = userController.getUserByHash(userHash);

        assertEquals(response.getStatus(), 200);
        assertEquals(response.getEntity(), mockUser);
    }

    @Test
    public void testGetUser_NotFound() {
        String userHash = "ABCDEF";
        when(userDAO.getUserByHash(userHash)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userController.getUserByHash(userHash));
        assertEquals(exception.getMessage(), "User not found");
    }
}
