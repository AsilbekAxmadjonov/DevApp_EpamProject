package org.example.capstoneproject.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Integer id;
    private String content;
    private Integer userId;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostResponse(Integer id, String content, String username,
                        LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.username = username;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


}
