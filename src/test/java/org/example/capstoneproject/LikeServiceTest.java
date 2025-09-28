package org.example.capstoneproject;


import org.example.capstoneproject.dto.request.LikeRequest;
import org.example.capstoneproject.dto.response.LikeResponse;
import org.example.capstoneproject.dto.response.UnlikeResponse;
import org.example.capstoneproject.entity.Like;
import org.example.capstoneproject.entity.Post;
import org.example.capstoneproject.entity.Unlike;
import org.example.capstoneproject.entity.User;
import org.example.capstoneproject.repository.LikeRepository;
import org.example.capstoneproject.repository.PostRepository;
import org.example.capstoneproject.repository.UnlikeRepository;
import org.example.capstoneproject.repository.UserRepository;
import org.example.capstoneproject.service.LikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;
    @Mock
    private UnlikeRepository unlikeRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LikeService likeService;

    private User user;
    private Post post;
    private LikeRequest likeRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);

        post = new Post();
        post.setId(10);

        likeRequest = new LikeRequest();
        likeRequest.setUserId(user.getId());
        likeRequest.setPostId(post.getId());
    }

    @Test
    void addLike_success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(likeRepository.findByUserAndPost(user, post)).thenReturn(Optional.empty());
        when(unlikeRepository.findByUserAndPost(user, post)).thenReturn(Optional.empty());
        when(likeRepository.countByPost(post)).thenReturn(1L);
        when(unlikeRepository.countByPost(post)).thenReturn(0L);

        LikeResponse response = likeService.addLike(likeRequest);

        assertNotNull(response);
        assertEquals("Liked post successfully", response.getMessage());
        assertEquals(1L, response.getLikeCount());
        verify(likeRepository).save(any(Like.class));
        verify(postRepository).save(post);
    }

    @Test
    void addLike_userAlreadyLiked_shouldThrowException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(likeRepository.findByUserAndPost(user, post)).thenReturn(Optional.of(new Like()));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> likeService.addLike(likeRequest));
        assertEquals("User already liked this post", ex.getMessage());
    }

    @Test
    void addUnlike_success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(likeRepository.findByUserAndPost(user, post)).thenReturn(Optional.empty());
        when(unlikeRepository.findByUserAndPost(user, post)).thenReturn(Optional.empty());
        when(likeRepository.countByPost(post)).thenReturn(0L);
        when(unlikeRepository.countByPost(post)).thenReturn(1L);

        UnlikeResponse response = likeService.addUnlike(likeRequest);

        assertNotNull(response);
        assertEquals("Unliked post successfully", response.getMessage());
        assertEquals(1L, response.getUnlikeCount());
        verify(unlikeRepository).save(any(Unlike.class));
        verify(postRepository).save(post);
    }

    @Test
    void addUnlike_userAlreadyUnliked_shouldThrowException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(unlikeRepository.findByUserAndPost(user, post)).thenReturn(Optional.of(new Unlike()));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> likeService.addUnlike(likeRequest));
        assertEquals("User already unliked this post", ex.getMessage());
    }
}

