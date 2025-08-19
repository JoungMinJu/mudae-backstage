package com.example.demo.dto;

import com.example.demo.entity.Vote;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class VoteDto {
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VoteRequest {
        @Min(value = 1, message = "유효하지 않은 게시글 ID입니다.")
        private Long postId;
        
        @Min(value = 1, message = "유효하지 않은 댓글 ID입니다.")
        private Long commentId;
        
        @NotNull(message = "투표 타입은 필수입니다.")
        private Vote.VoteType voteType;
        
        // Post나 Comment 중 하나만 있어야 함
        @AssertTrue(message = "게시글 ID와 댓글 ID 중 하나만 제공해야 합니다.")
        public boolean isEitherPostOrComment() {
            return (postId != null && commentId == null) || (postId == null && commentId != null);
        }
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VoteResponse {
        private Long id;
        private UserDto.UserResponse user;
        private Long postId;
        private Long commentId;
        private Vote.VoteType voteType;
        private LocalDateTime createdAt;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VoteSummary {
        private Long postId;
        private Long commentId;
        private Long likeCount;
        private Long dislikeCount;
        private Long agreeCount;
        private Long disagreeCount;
        private Vote.VoteType userVote;
    }
}
