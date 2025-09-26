package org.example.capstoneproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.capstoneproject.dto.response.UserResponse;
import org.example.capstoneproject.entity.User;
import org.example.capstoneproject.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // Update user details
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUserDetails(
            @PathVariable Integer id,
            @RequestBody User request) {
        UserResponse updated = userService.updateUserDetails(id, request);
        return ResponseEntity.ok(updated);
    }

    // Fetch user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
