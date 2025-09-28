package org.example.capstoneproject;


import org.example.capstoneproject.controller.CommentController;
import org.example.capstoneproject.dto.request.CommentRequest;
import org.example.capstoneproject.dto.response.CommentResponse;
import org.example.capstoneproject.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private CommentRequest request;
    private CommentResponse response;

    @BeforeEach
    void setUp() {
        request = new CommentRequest();
        request.setContent("This is a test comment");

        response = new CommentResponse();
        response.setId(1);
        response.setContent("This is a test comment");
        response.setPostId(10);
        response.setUserId(5);
        response.setUsername("testuser");
    }

    @Test
    void createComment_shouldReturnCreatedResponse() {
        // Mock SecurityContext for this test only
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);

        when(commentService.createComment(10, "testuser", request)).thenReturn(response);

        ResponseEntity<CommentResponse> result = commentController.createComment(10, request);

        assertEquals(201, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
        verify(commentService, times(1)).createComment(10, "testuser", request);
    }

    @Test
    void updateComment_shouldReturnOkResponse() {
        // Mock SecurityContext for this test only
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);

        when(commentService.updateComment(1, "testuser", request)).thenReturn(response);

        ResponseEntity<CommentResponse> result = commentController.updateComment(1, request);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
        verify(commentService, times(1)).updateComment(1, "testuser", request);
    }

    @Test
    void getCommentsByPost_shouldReturnListOfComments() {
        List<CommentResponse> list = Collections.singletonList(response);
        when(commentService.getCommentsByPost(10)).thenReturn(list);

        ResponseEntity<List<CommentResponse>> result = commentController.getCommentsByPost(10);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(1, result.getBody().size());
        assertEquals(response, result.getBody().get(0));
        verify(commentService, times(1)).getCommentsByPost(10);
    }

    @Test
    void deleteComment_shouldReturnNoContent() {
        // Mock SecurityContext for this test only
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);

        doNothing().when(commentService).deleteComment(1, "testuser");

        ResponseEntity<Void> result = commentController.deleteComment(1);

        assertEquals(204, result.getStatusCodeValue());
        verify(commentService, times(1)).deleteComment(1, "testuser");
    }
}

