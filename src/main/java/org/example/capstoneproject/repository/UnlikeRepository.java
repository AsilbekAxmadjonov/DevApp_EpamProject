package org.example.capstoneproject.repository;


import org.example.capstoneproject.entity.Unlike;
import org.example.capstoneproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.example.capstoneproject.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnlikeRepository extends JpaRepository<Unlike, Integer> {
    Optional<Unlike> findByUserAndPost(User user, Post post);;
    long countByPost(Post post);
}
