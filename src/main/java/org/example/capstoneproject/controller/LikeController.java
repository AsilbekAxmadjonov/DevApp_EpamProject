package org.example.capstoneproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.capstoneproject.dto.request.LikeRequest;
import org.example.capstoneproject.dto.response.LikeResponse;
import org.example.capstoneproject.dto.response.UnlikeResponse;
import org.example.capstoneproject.repository.LikeRepository;
import org.example.capstoneproject.repository.PostRepository;
import org.example.capstoneproject.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.capstoneproject.entity.Post;

@RestController
@RequestMapping("api/v1/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    private final PostRepository postRepository;

    @PostMapping("/like")
    public ResponseEntity<LikeResponse> likePost(@RequestBody LikeRequest request){
        return ResponseEntity.ok(likeService.addLike(request));
    }

    @PostMapping("/unlike")
    public ResponseEntity<UnlikeResponse> unLikePost(@RequestBody LikeRequest request){
        return ResponseEntity.ok(likeService.addUnlike(request));
    }

    @GetMapping("post/{postId}/likesCount")
    public long getLikesCount(@PathVariable Integer postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return likeService.getLikesCount(post);
    }

    @GetMapping("post/{postId}/unlikesCount")
    public long getUnlikesCount(@PathVariable Integer postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return likeService.getUnlikesCount(post);
    }
}
