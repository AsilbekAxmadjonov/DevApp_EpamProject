package org.example.capstoneproject.service;

import lombok.RequiredArgsConstructor;
import org.example.capstoneproject.dto.request.LoginRequest;
import org.example.capstoneproject.dto.request.RegisterRequest;
import org.example.capstoneproject.dto.response.AuthResponse;
import org.example.capstoneproject.entity.User;
import org.example.capstoneproject.repository.UserRepository;
import org.example.capstoneproject.service.JwtService;
import org.example.capstoneproject.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final Validation validation;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        if(validation.validateEmail(request.getEmail())){
            user.setEmail(request.getEmail());
        }else{
            throw new IllegalArgumentException("Invalid email format. Example: user@example.com");
        }

        if(validation.validatePhoneNumber(request.getPhone())){
            user.setPhone(request.getPhone());
        }else{
            throw new IllegalArgumentException(("Invalid phone format. example: +998901760112"));
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        User savedUser = userRepository.save(user);

        UserDetails userDetails = userService.loadUserByUsername(savedUser.getUsername());
        String jwtToken = jwtService.generateToken(userDetails);

        return new AuthResponse(jwtToken,user.getFirstName(), user.getLastName(), savedUser.getId(), savedUser.getUsername(),
                savedUser.getEmail());
    }

    public AuthResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        String jwtToken = jwtService.generateToken(userDetails);

        return new AuthResponse(jwtToken,  user.getFirstName(), user.getLastName(),user.getId(), user.getUsername(),
                user.getEmail());
    }
}