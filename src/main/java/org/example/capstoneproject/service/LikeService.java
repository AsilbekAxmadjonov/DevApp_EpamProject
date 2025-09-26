package org.example.capstoneproject.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.capstoneproject.dto.request.LikeRequest;
import org.example.capstoneproject.dto.response.LikeResponse;
import org.example.capstoneproject.dto.response.UnlikeResponse;
import org.example.capstoneproject.entity.Like;
import org.example.capstoneproject.entity.Unlike;
import org.example.capstoneproject.entity.User;
import org.example.capstoneproject.repository.LikeRepository;
import org.example.capstoneproject.repository.PostRepository;
import org.example.capstoneproject.repository.UnlikeRepository;
import org.example.capstoneproject.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.example.capstoneproject.entity.Post;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UnlikeRepository unlikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

        public LikeResponse addLike(LikeRequest request){
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Post post = postRepository.findById(request.getPostId())
                    .orElseThrow(() -> new RuntimeException("Post not found"));

        unlikeRepository.findByUserAndPost(user, post).ifPresent(unlikeRepository::delete);

        if(likeRepository.findByUserAndPost(user ,post).isPresent()){
            throw new RuntimeException("User already liked this post");
        }

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        likeRepository.save(like);

        long likeCount = likeRepository.countByPost(post);
        long unlikeCount = unlikeRepository.countByPost(post);

        like.setLikeCount(likeCount);
        post.setLikeCount(likeCount);
        post.setUnlikeCount(unlikeCount);
        postRepository.save(post);

        return new LikeResponse("Liked post successfully", likeCount);
    }

    public UnlikeResponse addUnlike(LikeRequest request){
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        likeRepository.findByUserAndPost(user, post).ifPresent(likeRepository::delete);

        if(unlikeRepository.findByUserAndPost(user, post).isPresent()) {
            throw new RuntimeException("User already unliked this post");
        }

        Unlike unlike = new Unlike();
        unlike.setUser(user);
        unlike.setPost(post);
        unlikeRepository.save(unlike);

        long likeCount = likeRepository.countByPost(post);
        long unlikeCount = unlikeRepository.countByPost(post);

        unlike.setUnlikeCount(unlikeCount);
        post.setUnlikeCount(unlikeCount);
        post.setLikeCount(likeCount);
        postRepository.save(post);

        long unLikeCount = unlikeRepository.countByPost(post);
        return new UnlikeResponse("Unlike post successfully", unLikeCount);
    }

    public long getLikesCount(Post post) {
        return likeRepository.countByPost(post);
    }

    public long getUnlikesCount(Post post) {
        return unlikeRepository.countByPost(post);
    }

}
