package com.example.demo.repository;

import com.example.demo.entity.Post;
import com.example.demo.entity.Post.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    Page<Post> findByPerformanceId(Long performanceId, Pageable pageable);
    
    Page<Post> findByPerformanceIdAndPostType(Long performanceId, PostType postType, Pageable pageable);
    
    Page<Post> findByPerformanceIdAndIsSpoiler(Long performanceId, Boolean isSpoiler, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.performance.id = :performanceId ORDER BY p.createdAt DESC")
    List<Post> findLatestPostsByPerformance(@Param("performanceId") Long performanceId);
    
    @Query("SELECT p FROM Post p WHERE p.performance.id = :performanceId ORDER BY p.likeCount DESC")
    List<Post> findTopPostsByPerformance(@Param("performanceId") Long performanceId);
    
    Page<Post> findByUserId(Long userId, Pageable pageable);
    
    Page<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
        String title, String content, Pageable pageable);
}
