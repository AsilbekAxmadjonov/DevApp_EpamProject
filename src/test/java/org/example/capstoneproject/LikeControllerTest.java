package org.example.capstoneproject;

import org.example.capstoneproject.controller.LikeController;
import org.example.capstoneproject.dto.request.LikeRequest;
import org.example.capstoneproject.dto.response.LikeResponse;
import org.example.capstoneproject.dto.response.UnlikeResponse;
import org.example.capstoneproject.entity.Post;
import org.example.capstoneproject.repository.PostRepository;
import org.example.capstoneproject.service.LikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeControllerTest {

    @Mock
    private LikeService likeService;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private LikeController likeController;

    private LikeRequest likeRequest;
    private LikeResponse likeResponse;
    private UnlikeResponse unlikeResponse;
    private Post post;

    @BeforeEach
    void setUp() {
        likeRequest = new LikeRequest();
        likeRequest.setUserId(1);
        likeRequest.setPostId(10);

        likeResponse = new LikeResponse("Liked post successfully", 5L);
        unlikeResponse = new UnlikeResponse("Unlike post successfully", 2L);

        post = new Post();
        post.setId(10);
    }

    @Test
    void likePost_shouldReturnLikeResponse() {
        when(likeService.addLike(likeRequest)).thenReturn(likeResponse);

        ResponseEntity<LikeResponse> result = likeController.likePost(likeRequest);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(likeResponse, result.getBody());
        verify(likeService, times(1)).addLike(likeRequest);
    }

    @Test
    void unLikePost_shouldReturnUnlikeResponse() {
        when(likeService.addUnlike(likeRequest)).thenReturn(unlikeResponse);

        ResponseEntity<UnlikeResponse> result = likeController.unLikePost(likeRequest);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(unlikeResponse, result.getBody());
        verify(likeService, times(1)).addUnlike(likeRequest);
    }

    @Test
    void getLikesCount_shouldReturnLikeCount() {
        when(postRepository.findById(10)).thenReturn(Optional.of(post));
        when(likeService.getLikesCount(post)).thenReturn(5L);

        long count = likeController.getLikesCount(10);

        assertEquals(5L, count);
        verify(postRepository, times(1)).findById(10);
        verify(likeService, times(1)).getLikesCount(post);
    }

    @Test
    void getUnlikesCount_shouldReturnUnlikeCount() {
        when(postRepository.findById(10)).thenReturn(Optional.of(post));
        when(likeService.getUnlikesCount(post)).thenReturn(2L);

        long count = likeController.getUnlikesCount(10);

        assertEquals(2L, count);
        verify(postRepository, times(1)).findById(10);
        verify(likeService, times(1)).getUnlikesCount(post);
    }

    @Test
    void getLikesCount_postNotFound_shouldThrowException() {
        when(postRepository.findById(10)).thenReturn(Optional.empty());

        try {
            likeController.getLikesCount(10);
        } catch (RuntimeException e) {
            assertEquals("Post not found", e.getMessage());
        }
    }

    @Test
    void getUnlikesCount_postNotFound_shouldThrowException() {
        when(postRepository.findById(10)).thenReturn(Optional.empty());

        try {
            likeController.getUnlikesCount(10);
        } catch (RuntimeException e) {
            assertEquals("Post not found", e.getMessage());
        }
    }
}

