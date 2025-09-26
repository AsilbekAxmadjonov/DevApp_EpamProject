package org.example.capstoneproject.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.capstoneproject.dto.request.CommentRequest;
import org.example.capstoneproject.dto.response.CommentResponse;
import org.example.capstoneproject.entity.Comment;
import org.example.capstoneproject.entity.User;
import org.example.capstoneproject.repository.CommentRepository;
import org.example.capstoneproject.repository.PostRepository;
import org.example.capstoneproject.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.example.capstoneproject.entity.Post;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse createComment(Integer postId, String username, CommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        System.out.println(username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println(user);
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(request.getContent());

        Comment saved = commentRepository.save(comment);
        return toResponse(saved);
    }

    @Transactional
    public CommentResponse updateComment(Integer commentId, String username, CommentRequest request){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if(!comment.getUser().getUsername().equals(username)){
            throw new RuntimeException("Not allowed to update this comment");
        }

        comment.setContent(request.getContent());
        Comment updated = commentRepository.save(comment);

        return toResponse(updated);
    }

    @Transactional
    public void deleteComment(Integer commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }

    public List<CommentResponse> getCommentsByPost(Integer postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(this::toResponse)
                .toList();
    }

    private CommentResponse toResponse(Comment c){
        CommentResponse r = new CommentResponse();

        r.setId(c.getId());
        r.setContent(c.getContent());
        r.setPostId(c.getPost() != null ? c.getPost().getId() : null);
        r.setUserId(c.getUser() != null ? c.getUser().getId() : null);
        r.setUsername(c.getUser() != null ? c.getUser().getUsername() : null);
        r.setCreatedAt(c.getCreatedAt());
        r.setUpdatedAt(c.getUpdatedAt());

        return r;
    }
}
