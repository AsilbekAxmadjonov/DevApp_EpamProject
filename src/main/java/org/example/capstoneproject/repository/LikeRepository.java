package org.example.capstoneproject.repository;

import org.example.capstoneproject.entity.Like;
import org.example.capstoneproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.capstoneproject.entity.Post;


import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByUserAndPost(User user, Post post);
    long countByPost(Post post);
}
