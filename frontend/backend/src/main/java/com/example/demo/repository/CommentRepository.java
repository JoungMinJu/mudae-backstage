package com.example.demo.repository;

import com.example.demo.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);
    
    List<Comment> findByPostIdAndParentIsNullOrderByCreatedAtAsc(Long postId);
    
    List<Comment> findByParentIdOrderByCreatedAtAsc(Long parentId);
    
    Page<Comment> findByUserId(Long userId, Pageable pageable);
    
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND c.depth = 0 ORDER BY c.createdAt ASC")
    List<Comment> findTopLevelCommentsByPost(@Param("postId") Long postId);
    
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId ORDER BY c.likeCount DESC")
    List<Comment> findTopCommentsByPost(@Param("postId") Long postId);
    
    long countByPostId(Long postId);
    
    long countByUserId(Long userId);
}
