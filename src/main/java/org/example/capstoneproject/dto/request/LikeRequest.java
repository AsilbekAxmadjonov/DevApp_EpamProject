package org.example.capstoneproject.dto.request;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

@Data
public class LikeRequest {
    private Integer userId;
    private Integer postId;
}
