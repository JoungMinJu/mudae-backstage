package com.example.demo.repository;

import com.example.demo.entity.Vote;
import com.example.demo.entity.Vote.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    
    Optional<Vote> findByUserIdAndPostId(Long userId, Long postId);
    
    Optional<Vote> findByUserIdAndCommentId(Long userId, Long commentId);
    
    List<Vote> findByPostIdAndVoteType(Long postId, VoteType voteType);
    
    List<Vote> findByCommentIdAndVoteType(Long commentId, VoteType voteType);
    
    @Query("SELECT COUNT(v) FROM Vote v WHERE v.post.id = :postId AND v.voteType = :voteType")
    long countByPostIdAndVoteType(@Param("postId") Long postId, @Param("voteType") VoteType voteType);
    
    @Query("SELECT COUNT(v) FROM Vote v WHERE v.comment.id = :commentId AND v.voteType = :voteType")
    long countByCommentIdAndVoteType(@Param("commentId") Long commentId, @Param("voteType") VoteType voteType);
    
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    
    boolean existsByUserIdAndCommentId(Long userId, Long commentId);
    
    void deleteByUserIdAndPostId(Long userId, Long postId);
    
    void deleteByUserIdAndCommentId(Long userId, Long commentId);
}
