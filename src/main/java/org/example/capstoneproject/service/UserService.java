package org.example.capstoneproject.service;

import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.example.capstoneproject.dto.response.UserResponse;
import org.example.capstoneproject.entity.User;
import org.example.capstoneproject.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final Validation validation;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

    public UserResponse getUserbyFirstName(String firstName){
        User user = userRepository.findByFirstName(firstName)
                .orElseThrow(() -> new RuntimeException("User not found with firstName: " + firstName));
        return mapToUserResponse(user);
    }

    public UserResponse getUserbyLastName(String lastName){
        User user = userRepository.findByFirstName(lastName)
                .orElseThrow(() -> new RuntimeException("User not found with lastname: " + lastName));
        return mapToUserResponse(user);
    }

    public UserResponse getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapToUserResponse(user);
    }

    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return mapToUserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    public UserResponse updateUserDetails(Integer id, User request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getPassword() != null) user.setPassword(request.getPassword());
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getProfilePhoto() != null && request.getProfilePhoto().length > 0) {
            user.setProfilePhoto(request.getProfilePhoto()); // already byte[]
        }

        if (request.getEmail() != null  ){
            if(validation.validateEmail(request.getEmail())){
                user.setEmail(request.getEmail());
            }else{
                throw new IllegalArgumentException("Invalid email format. Example: user@example.com");
            }
        }

        if (request.getPhone() != null) {
            if (validation.validatePhoneNumber(request.getPhone())) {
                user.setPhone(request.getPhone());
            } else {
                throw new IllegalArgumentException("Invalid phone format. Example: +998901760112");
            }
        }


        User saved = userRepository.save(user);

        return mapToUserResponse(saved);
    }



    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setBio(user.getBio());
        if (user.getProfilePhoto() != null && user.getProfilePhoto().length > 0) {
            response.setProfilePhoto(Base64.getEncoder().encodeToString(user.getProfilePhoto()));
        } else {
            response.setProfilePhoto(null); // or leave empty
        }
        return response;
    }
    public User getUserEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}