package org.example.capstoneproject.dto.response;


import lombok.Data;

@Data
public class UserResponse {
    private String firstName;
    private String lastName;
    private Integer id;
    private String username;
    private String email;
    private String phone;
}