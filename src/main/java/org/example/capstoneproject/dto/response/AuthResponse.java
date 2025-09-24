package org.example.capstoneproject.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private String firstName;
    private String lastName;
    private Integer userId;
    private String username;
    private String email;

    public AuthResponse(String token, String firstName, String lastName,Integer userId, String username, String email) {
        this.token = token;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.username = username;
        this.email = email;
    }
}
