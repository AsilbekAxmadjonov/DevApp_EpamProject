package org.example.capstoneproject.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private Integer userId;
    private String username;
    private String email;

    public AuthResponse(String token, Integer userId, String username, String email) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.email = email;
    }
}
