package org.example.capstoneproject.dto.response;


import lombok.Data;

@Data
public class UserResponse {
    private Integer id;
    private String username;
    private String email;
    private String phone;
}