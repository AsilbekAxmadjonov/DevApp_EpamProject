package org.example.capstoneproject;

import org.example.capstoneproject.dto.response.UserResponse;
import org.example.capstoneproject.entity.User;
import org.example.capstoneproject.repository.UserRepository;
import org.example.capstoneproject.service.UserService;
import org.example.capstoneproject.service.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Validation validation;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);
        user.setFirstName("Asilbek");
        user.setLastName("Axmadjonov");
        user.setUsername("asilbek");
        user.setPassword("secret");
        user.setEmail("asilbek@example.com");
        user.setPhone("+998901760112");
        user.setBio("Developer");
        user.setProfilePhoto("hello".getBytes());
    }

    @Test
    void loadUserByUsername_success() {
        when(userRepository.findByUsername("asilbek")).thenReturn(Optional.of(user));

        UserDetails details = userService.loadUserByUsername("asilbek");

        assertEquals("asilbek", details.getUsername());
        assertEquals("secret", details.getPassword());
    }

    @Test
    void loadUserByUsername_userNotFound() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("missing"));
    }

    @Test
    void getUserById_success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserResponse response = userService.getUserById(1);

        assertEquals("Asilbek", response.getFirstName());
        assertEquals("Axmadjonov", response.getLastName());
        assertEquals(Base64.getEncoder().encodeToString("hello".getBytes()), response.getProfilePhoto());
    }

    @Test
    void getUserById_notFound() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserById(99));
    }

    @Test
    void getAllUsers_success() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponse> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("Asilbek", result.get(0).getFirstName());
    }

    @Test
    void updateUserDetails_validEmailAndPhone() {
        User request = new User();
        request.setFirstName("NewName");
        request.setEmail("new@example.com");
        request.setPhone("+998911234567");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(validation.validateEmail("new@example.com")).thenReturn(true);
        when(validation.validatePhoneNumber("+998911234567")).thenReturn(true);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.updateUserDetails(1, request);

        assertEquals("NewName", response.getFirstName());
        assertEquals("new@example.com", response.getEmail());
        assertEquals("+998911234567", response.getPhone());
    }

    @Test
    void updateUserDetails_invalidEmail_throwsException() {
        User request = new User();
        request.setEmail("invalidEmail");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(validation.validateEmail("invalidEmail")).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUserDetails(1, request));
    }

    @Test
    void updateUserDetails_invalidPhone_throwsException() {
        User request = new User();
        request.setPhone("12345");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(validation.validatePhoneNumber("12345")).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUserDetails(1, request));
    }

    @Test
    void getUserEntityByEmail_success() {
        when(userRepository.findByEmail("asilbek@example.com")).thenReturn(Optional.of(user));

        User found = userService.getUserEntityByEmail("asilbek@example.com");

        assertEquals("asilbek", found.getUsername());
    }

    @Test
    void getUserEntityByEmail_notFound() {
        when(userRepository.findByEmail("none@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.getUserEntityByEmail("none@example.com"));
    }
}
