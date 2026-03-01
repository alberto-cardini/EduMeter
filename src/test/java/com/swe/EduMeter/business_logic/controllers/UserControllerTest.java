package com.swe.EduMeter.business_logic.controllers;

import com.swe.EduMeter.model.User;
import com.swe.EduMeter.model.response.ApiOk;
import com.swe.EduMeter.orm.UserDAO;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserController userController;

    @Test
    public void testSearch() {
        // Testing with a specific boolean filter
        Boolean searchBanned = true;
        List<User> expectedUsers = List.of(
                new User("hash_1", true),
                new User("hash_2", true)
        );

        when(userDAO.search(searchBanned)).thenReturn(expectedUsers);

        List<User> gotUsers = userController.search(searchBanned);

        assertEquals(expectedUsers.size(), gotUsers.size());
        assertEquals(expectedUsers.get(0).getHash(), gotUsers.get(0).getHash());
        verify(userDAO, times(1)).search(searchBanned);
    }

    @Test
    public void testBan_Found() {
        String userHash = "target_user_hash";
        User user = new User(userHash, false);

        when(userDAO.get(userHash)).thenReturn(Optional.of(user));

        ApiOk response = userController.ban(userHash);

        assertEquals("User banned", response.message());

        // Verify the controller mutated the user object properly
        assertTrue(user.isBanned());

        // Verify the DAO was called to persist the mutated object
        verify(userDAO, times(1)).update(user);
    }

    @Test
    public void testBan_NotFound() {
        String userHash = "unknown_user_hash";

        when(userDAO.get(userHash)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userController.ban(userHash));

        assertEquals("User not found", exception.getMessage());

        // Ensure no update is attempted if the user doesn't exist
        verify(userDAO, never()).update(any(User.class));
    }
}