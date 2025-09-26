    package org.example.capstoneproject.dto.response;

    import lombok.AllArgsConstructor;
    import lombok.Data;

    @Data
    @AllArgsConstructor
    public class UnlikeResponse {
        private String message;
        private long unlikeCount;
    }
