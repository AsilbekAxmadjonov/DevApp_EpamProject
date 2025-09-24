package org.example.capstoneproject.service;


import lombok.RequiredArgsConstructor;
import org.example.capstoneproject.dto.request.PostRequest;
import org.example.capstoneproject.dto.response.PostResponse;
import org.example.capstoneproject.entity.Post;
import org.example.capstoneproject.entity.User;
import org.example.capstoneproject.mapper.PostMapper;
import org.example.capstoneproject.repository.PostRepository;
import org.example.capstoneproject.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponse createPost(PostRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found" + request.getUserId()));

        Post post = new Post();
        post.setUser(user);
        post.setContent(request.getContent());

        List<Post> posts = user.getPosts();
        posts.add(post);
        user.setPosts(posts);

        Post saved = postRepository.save(post);

        return new PostResponse(
                saved.getId(),
                saved.getContent(),
                saved.getUser().getId(),
                saved.getUser().getUsername(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    public List<PostResponse> getaAllPosts(){
        List<Post> posts = postRepository.findAll();

        return posts.stream()
                .map(post -> new PostResponse(
                        post.getId(),
                        post.getContent(),
                        post.getUser().getUsername(),
                        post.getCreatedAt(),
                        post.getUpdatedAt()
                ))
                .toList();
    }


    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    public PostResponse updatePost(Long id, String newContent) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setContent(newContent);
        Post updated = postRepository.save(post);

        return new PostResponse(
                updated.getId(),
                updated.getContent(),
                updated.getUser().getId(),
                updated.getUser().getUsername(),
                updated.getCreatedAt(),
                updated.getUpdatedAt()
        );
    }

    public ResponseEntity<?> getAllPostsByUserId(Integer userId){
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()){
            return ResponseEntity.ofNullable("user not found");
        }

        return ResponseEntity.ok(PostMapper.toDtoList(user.get().getPosts()));
    }
}
