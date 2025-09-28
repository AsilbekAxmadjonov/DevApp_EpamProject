package org.example.capstoneproject;


import org.example.capstoneproject.dto.request.PostRequest;
import org.example.capstoneproject.dto.response.PostResponse;
import org.example.capstoneproject.entity.Post;
import org.example.capstoneproject.entity.User;
import org.example.capstoneproject.mapper.PostMapper;
import org.example.capstoneproject.repository.PostRepository;
import org.example.capstoneproject.repository.UserRepository;
import org.example.capstoneproject.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);
        user.setUsername("asilbek");
        user.setPosts(new ArrayList<>());

        post = new Post();
        post.setId(100);
        post.setContent("Hello World");
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        user.getPosts().add(post);
    }

    @Test
    void createPost_success() {
        PostRequest request = new PostRequest();
        request.setUserId(1);
        request.setContent("New Post");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(postRepository.save(any(Post.class))).thenAnswer(inv -> {
            Post p = inv.getArgument(0);
            p.setId(200);
            return p;
        });

        PostResponse response = postService.createPost(request);

        assertNotNull(response);
        assertEquals("New Post", response.getContent());
        assertEquals(1, response.getUserId());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void createPost_userNotFound_throwsException() {
        PostRequest request = new PostRequest();
        request.setUserId(999);
        request.setContent("Fail");

        when(userRepository.findById(999)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> postService.createPost(request));

        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void getAllPosts_success() {
        when(postRepository.findAll()).thenReturn(List.of(post));

        List<PostResponse> responses = postService.getaAllPosts();

        assertEquals(1, responses.size());
        assertEquals("Hello World", responses.get(0).getContent());
    }

    @Test
    void getPostById_success() {
        when(postRepository.findById(100)).thenReturn(Optional.of(post));

        PostResponse response = postService.getPostById(100);

        assertEquals(100, response.getId());
        assertEquals("Hello World", response.getContent());
        assertEquals("asilbek", response.getUsername());
    }

    @Test
    void getPostById_notFound() {
        when(postRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> postService.getPostById(999));
    }

    @Test
    void updatePost_success() {
        when(postRepository.findById(100)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenAnswer(inv -> inv.getArgument(0));

        PostResponse response = postService.updatePost(100, "Updated Content");

        assertEquals("Updated Content", response.getContent());
        verify(postRepository).save(post);
    }

    @Test
    void updatePost_notFound() {
        when(postRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> postService.updatePost(999, "Doesn't exist"));
    }

    @Test
    void getAllPostsByUserId_success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        ResponseEntity<?> response = postService.getAllPostsByUserId(1);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody() instanceof List<?>);
    }

    @Test
    void getAllPostsByUserId_userNotFound() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        ResponseEntity<?> response = postService.getAllPostsByUserId(999);

        assertEquals(200, response.getStatusCode().value()); // ResponseEntity.ofNullable returns 200 w/ null
        assertEquals("user not found", response.getBody());
    }
}

