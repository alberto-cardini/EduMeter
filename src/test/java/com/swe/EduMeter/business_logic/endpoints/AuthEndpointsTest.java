package com.swe.EduMeter.business_logic.endpoints;

import com.swe.EduMeter.business_logic.auth.CryptoService;
import com.swe.EduMeter.models.Admin;
import com.swe.EduMeter.models.PinChallenge;
import com.swe.EduMeter.models.User;
import com.swe.EduMeter.models.response.ApiOk;
import com.swe.EduMeter.orm.dao.AdminDAO;
import com.swe.EduMeter.orm.dao.PinChallengeDAO;
import com.swe.EduMeter.orm.dao.UserDAO;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthEndpointsTest {

    @Mock
    private PinChallengeDAO pinDAO;

    @Mock
    private AdminDAO adminDAO;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private AuthEndpoints authEndpoints;

    @Test
    public void testSendPin_MissingEmail() {
        AuthEndpoints.SendPinBody body = new AuthEndpoints.SendPinBody(null);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> authEndpoints.sendPin(false, body));

        assertEquals("Missing field 'email'", exception.getMessage());
    }

    @Test
    public void testSendPin_Admin_Valid() {
        String email = "admin@edumeter.com";
        AuthEndpoints.SendPinBody body = new AuthEndpoints.SendPinBody(email);

        when(adminDAO.getByEmail(email)).thenReturn(Optional.of(new Admin(1, email)));
        when(pinDAO.add(any(PinChallenge.class))).thenReturn(42);

        CryptoService mockCrypto = mock(CryptoService.class);
        when(mockCrypto.getUserId(email)).thenReturn("admin_hash");

        try (MockedStatic<CryptoService> mockedStatic = mockStatic(CryptoService.class)) {
            mockedStatic.when(CryptoService::getInstance).thenReturn(mockCrypto);

            var response = authEndpoints.sendPin(true, body);

            assertEquals("SendPinResponse[challengeId=42]", response.toString());
            verify(pinDAO, times(1)).add(any(PinChallenge.class));
        }
    }

    @Test
    public void testSendPin_Admin_InvalidEmail() {
        String email = "fakeadmin@edumeter.com";
        AuthEndpoints.SendPinBody body = new AuthEndpoints.SendPinBody(email);

        when(adminDAO.getByEmail(email)).thenReturn(Optional.empty());

        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> authEndpoints.sendPin(true, body));

        assertEquals("Invalid admin email", exception.getMessage());
        verify(pinDAO, never()).add(any(PinChallenge.class));
    }

    @Test
    public void testSendPin_Student_ValidDomain() {
        String email = "student@edu.unifi.it";
        AuthEndpoints.SendPinBody body = new AuthEndpoints.SendPinBody(email);

        when(pinDAO.add(any(PinChallenge.class))).thenReturn(99);

        CryptoService mockCrypto = mock(CryptoService.class);
        when(mockCrypto.getUserId(email)).thenReturn("student_hash");

        try (MockedStatic<CryptoService> mockedStatic = mockStatic(CryptoService.class)) {
            mockedStatic.when(CryptoService::getInstance).thenReturn(mockCrypto);

            var response = authEndpoints.sendPin(false, body);

            assertEquals("SendPinResponse[challengeId=99]", response.toString());
            verify(pinDAO, times(1)).add(any(PinChallenge.class));
        }
    }

    @Test
    public void testSendPin_Student_InvalidDomain() {
        String email = "student@gmail.com";
        AuthEndpoints.SendPinBody body = new AuthEndpoints.SendPinBody(email);

        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> authEndpoints.sendPin(false, body));

        assertEquals("Invalid email domain", exception.getMessage());
        verify(pinDAO, never()).add(any(PinChallenge.class));
    }

    @Test
    public void testLogin_Valid() {
        int challengeId = 42;
        String pin = "1234";
        String userHash = "user_hash";
        AuthEndpoints.LoginBody body = new AuthEndpoints.LoginBody(challengeId, pin);

        // Instant in the future to simulate unexpired pin
        PinChallenge challenge = new PinChallenge(challengeId, pin, userHash, Instant.now().plusSeconds(600), false);
        when(pinDAO.get(challengeId)).thenReturn(Optional.of(challenge));

        CryptoService mockCrypto = mock(CryptoService.class);
        String expectedToken = "encoded_jwt_token";
        when(mockCrypto.generateToken(userHash, false)).thenReturn(expectedToken);

        when(userDAO.get(userHash)).thenReturn(Optional.of(new User(userHash, false)));

        try (MockedStatic<CryptoService> mockedStatic = mockStatic(CryptoService.class)) {
            mockedStatic.when(CryptoService::getInstance).thenReturn(mockCrypto);

            var response = authEndpoints.login(false, body);

            assertEquals("LoginResponse[token=" + expectedToken + "]", response.toString());
        }
    }

    @Test
    public void testLogin_Banned() {
        int challengeId = 42;
        String pin = "1234";
        String userHash = "user_hash";
        AuthEndpoints.LoginBody body = new AuthEndpoints.LoginBody(challengeId, pin);

        // Instant in the future to simulate unexpired pin
        PinChallenge challenge = new PinChallenge(challengeId, pin, userHash, Instant.now().plusSeconds(600), false);
        when(pinDAO.get(challengeId)).thenReturn(Optional.of(challenge));

        CryptoService mockCrypto = mock(CryptoService.class);

        when(userDAO.get(userHash)).thenReturn(Optional.of(new User(userHash, true)));

        try (MockedStatic<CryptoService> mockedStatic = mockStatic(CryptoService.class)) {
            mockedStatic.when(CryptoService::getInstance).thenReturn(mockCrypto);

            ForbiddenException exception = assertThrows(ForbiddenException.class, () -> authEndpoints.login(false, body));

            assertEquals("User is banned", exception.getMessage());
        }
    }

    @Test
    public void testLogin_ExpiredPin() {
        int challengeId = 42;
        String pin = "1234";
        AuthEndpoints.LoginBody body = new AuthEndpoints.LoginBody(challengeId, pin);

        // Instant in the past to simulate expired pin
        PinChallenge challenge = new PinChallenge(challengeId, pin, "hash", Instant.now().minusSeconds(600), false);
        when(pinDAO.get(challengeId)).thenReturn(Optional.of(challenge));

        NotAuthorizedException exception = assertThrows(NotAuthorizedException.class,
                () -> authEndpoints.login(false, body));

        assertEquals("Pin challenge expired", exception.getMessage());
    }

    @Test
    public void testLogin_WrongPin() {
        int challengeId = 42;
        AuthEndpoints.LoginBody body = new AuthEndpoints.LoginBody(challengeId, "9999"); // Wrong pin

        PinChallenge challenge = new PinChallenge(challengeId, "1234", "hash", Instant.now().plusSeconds(600), false);
        when(pinDAO.get(challengeId)).thenReturn(Optional.of(challenge));

        NotAuthorizedException exception = assertThrows(NotAuthorizedException.class,
                () -> authEndpoints.login(false, body));

        assertEquals("Invalid Pin", exception.getMessage());
    }

    @Test
    public void testLogin_MissingFields() {
        AuthEndpoints.LoginBody missingPinBody = new AuthEndpoints.LoginBody(42, null);
        BadRequestException e1 = assertThrows(BadRequestException.class, () -> authEndpoints.login(false, missingPinBody));
        assertEquals("Missing field 'pin'", e1.getMessage());

        AuthEndpoints.LoginBody missingIdBody = new AuthEndpoints.LoginBody(null, "1234");
        BadRequestException e2 = assertThrows(BadRequestException.class, () -> authEndpoints.login(false, missingIdBody));
        assertEquals("Missing field 'challengeId'", e2.getMessage());
    }

    @Test
    public void testLogin_NotFound() {
        int challengeId = 42;
        AuthEndpoints.LoginBody body = new AuthEndpoints.LoginBody(challengeId, "1234");

        when(pinDAO.get(challengeId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> authEndpoints.login(false, body));

        assertEquals("Invalid challengeId", exception.getMessage());
    }

    @Test
    public void testLogout() {
        String token = "my_secret_token";
        String bearer = "Bearer " + token;

        CryptoService mockCrypto = mock(CryptoService.class);

        try (MockedStatic<CryptoService> mockedStatic = mockStatic(CryptoService.class)) {
            mockedStatic.when(CryptoService::getInstance).thenReturn(mockCrypto);

            ApiOk response = authEndpoints.logout(bearer);

            assertEquals("Token revoked", response.message());
            verify(mockCrypto, times(1)).revokeToken(token);
        }
    }
}