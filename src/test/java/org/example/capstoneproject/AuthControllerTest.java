package org.example.capstoneproject;

import org.example.capstoneproject.controller.AuthController;
import org.example.capstoneproject.dto.request.LoginRequest;
import org.example.capstoneproject.dto.request.RegisterRequest;
import org.example.capstoneproject.dto.response.AuthResponse;
import org.example.capstoneproject.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthController authController;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        authResponse = new AuthResponse(
                "jwt-token",
                "Test",
                "User",
                1,
                "testuser",
                "test@example.com"
        );
    }

    @Test
    void register_shouldReturnCreatedResponse() {
        when(authenticationService.register(registerRequest)).thenReturn(authResponse);

        ResponseEntity<AuthResponse> response = authController.register(registerRequest);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(authResponse, response.getBody());
        verify(authenticationService, times(1)).register(registerRequest);
    }

    @Test
    void login_shouldReturnOkResponse() {
        when(authenticationService.authenticate(loginRequest)).thenReturn(authResponse);

        ResponseEntity<AuthResponse> response = authController.authenticate(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(authResponse, response.getBody());
        verify(authenticationService, times(1)).authenticate(loginRequest);
    }
}

