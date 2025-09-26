package org.example.capstoneproject.dto.response;

import lombok.Data;

@Data
public class ProfileUpdateResponse {
    private String message;
    private String bio;
    private String profilePhotoUrl;
}
