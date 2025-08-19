package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "votes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoteType voteType;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public enum VoteType {
        LIKE, DISLIKE, AGREE, DISAGREE
    }
    
    // Post나 Comment 중 하나만 참조해야 함
    @PreUpdate
    private void validateReferences() {
        if ((post == null && comment == null) || (post != null && comment != null)) {
            throw new IllegalStateException("Vote must reference either a post or a comment, but not both or neither");
        }
    }
}
