package org.example.capstoneproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostRequest {

    private Integer userId;

    @NotBlank(message = "Content cannot be empty")
    private String content;
}
