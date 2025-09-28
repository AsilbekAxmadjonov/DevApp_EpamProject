package org.example.capstoneproject;

import org.example.capstoneproject.dto.request.LoginRequest;
import org.example.capstoneproject.dto.request.RegisterRequest;
import org.example.capstoneproject.dto.response.AuthResponse;
import org.example.capstoneproject.entity.User;
import org.example.capstoneproject.repository.UserRepository;
import org.example.capstoneproject.service.AuthenticationService;
import org.example.capstoneproject.service.JwtService;
import org.example.capstoneproject.service.UserService;
import org.example.capstoneproject.service.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock private Validation validation;
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserService userService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_ShouldCreateUser_WhenValidRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("john");
        request.setPassword("password");
        request.setEmail("john@example.com");
        request.setPhone("+998901760112");
        request.setFirstName("John");
        request.setLastName("Doe");

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setUsername("john");
        savedUser.setEmail("john@example.com");

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(validation.validateEmail("john@example.com")).thenReturn(true);
        when(validation.validatePhoneNumber("+998901760112")).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDetails userDetails = mock(UserDetails.class);
        when(userService.loadUserByUsername("john")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("jwtToken");

        AuthResponse response = authenticationService.register(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());

        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(userDetails);
    }

    @Test
    void register_ShouldThrow_WhenUsernameExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("john");

        when(userRepository.existsByUsername("john")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authenticationService.register(request);
        });

        assertTrue(ex.getMessage().contains("Username already exists"));
    }

    @Test
    void authenticate_ShouldReturnAuthResponse_WhenValidCredentials() {
        LoginRequest request = new LoginRequest();
        request.setUsername("john");
        request.setPassword("password");

        User user = new User();
        user.setId(1);
        user.setUsername("john");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        UserDetails userDetails = mock(UserDetails.class);
        when(userService.loadUserByUsername("john")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("jwtToken");

        AuthResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals("john", response.getUsername());

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("john", "password")
        );
    }
}

