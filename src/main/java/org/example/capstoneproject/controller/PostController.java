package org.example.capstoneproject.controller;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.example.capstoneproject.dto.request.PostRequest;
import org.example.capstoneproject.dto.response.PostResponse;
import org.example.capstoneproject.entity.Post;
import org.example.capstoneproject.entity.User;
import org.example.capstoneproject.service.PostService;
import org.example.capstoneproject.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @GetMapping
    public List<PostResponse> getAllPosts(){
        return postService.getaAllPosts();
    }

    @PostMapping()
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest request){
        PostResponse post = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        PostResponse post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{id}")
    ResponseEntity<PostResponse> updatePost(
            @PathVariable Long id,
            @RequestBody PostRequest request
    ) {
        PostResponse updated = postService.updatePost(id, request.getContent());
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/getAllPostsByUserId")
    ResponseEntity<?> getAllPostsByUserId(@RequestParam(value = "id",required = true) Integer userId){
        return postService.getAllPostsByUserId(userId);
    }

}
