package org.example.capstoneproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstoneproject.dto.request.CommentRequest;
import org.example.capstoneproject.dto.response.CommentResponse;
import org.example.capstoneproject.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/posts/{postId}/create")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Integer postId,
            @Valid @RequestBody CommentRequest request
            ){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        CommentResponse created = commentService.createComment(postId, username, request);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Integer id,
            @Valid @RequestBody CommentRequest request) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        CommentResponse updated = commentService.updateComment(id, username, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/posts/{postId}/getCommentsByPost")
    public ResponseEntity<List<CommentResponse>> getCommentsByPost(@PathVariable Integer postId) {
        List<CommentResponse> list = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Integer id) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        commentService.deleteComment(id, username);
        return ResponseEntity.noContent().build();
    }
}
