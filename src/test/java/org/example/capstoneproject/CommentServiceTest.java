package org.example.capstoneproject;

import org.example.capstoneproject.dto.request.CommentRequest;
import org.example.capstoneproject.dto.response.CommentResponse;
import org.example.capstoneproject.entity.Comment;
import org.example.capstoneproject.entity.Post;
import org.example.capstoneproject.entity.User;
import org.example.capstoneproject.repository.CommentRepository;
import org.example.capstoneproject.repository.PostRepository;
import org.example.capstoneproject.repository.UserRepository;
import org.example.capstoneproject.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;

    private User user;
    private Post post;
    private Comment comment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);
        user.setUsername("testUser");

        post = new Post();
        post.setId(10);

        comment = new Comment();
        comment.setId(100);
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent("initial content");
    }

    @Test
    void createComment_success() {
        CommentRequest request = new CommentRequest();
        request.setContent("new comment");

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> {
            Comment saved = inv.getArgument(0);
            saved.setId(101);
            return saved;
        });

        CommentResponse response = commentService.createComment(post.getId(), user.getUsername(), request);

        assertNotNull(response);
        assertEquals("new comment", response.getContent());
        assertEquals(post.getId(), response.getPostId());
        assertEquals(user.getId(), response.getUserId());
        assertEquals(user.getUsername(), response.getUsername());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void createComment_postNotFound() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                commentService.createComment(post.getId(), user.getUsername(), new CommentRequest()));
    }

    @Test
    void createComment_userNotFound() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                commentService.createComment(post.getId(), user.getUsername(), new CommentRequest()));
    }

    @Test
    void updateComment_success() {
        CommentRequest request = new CommentRequest();
        request.setContent("updated content");

        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentResponse response = commentService.updateComment(comment.getId(), user.getUsername(), request);

        assertEquals("updated content", response.getContent());
        verify(commentRepository).save(comment);
    }

    @Test
    void updateComment_notOwner_throwsException() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        assertThrows(RuntimeException.class, () ->
                commentService.updateComment(comment.getId(), "otherUser", new CommentRequest()));
    }

    @Test
    void deleteComment_success() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        commentService.deleteComment(comment.getId(), user.getUsername());

        verify(commentRepository).delete(comment);
    }

    @Test
    void deleteComment_notOwner_throwsException() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        assertThrows(RuntimeException.class, () ->
                commentService.deleteComment(comment.getId(), "otherUser"));
    }

    @Test
    void getCommentsByPost_success() {
        when(commentRepository.findByPostId(post.getId())).thenReturn(Arrays.asList(comment));

        List<CommentResponse> responses = commentService.getCommentsByPost(post.getId());

        assertEquals(1, responses.size());
        assertEquals(comment.getContent(), responses.get(0).getContent());
        assertEquals(user.getUsername(), responses.get(0).getUsername());
    }
}

