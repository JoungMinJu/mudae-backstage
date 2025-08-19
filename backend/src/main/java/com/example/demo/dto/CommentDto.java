package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class CommentDto {
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "댓글 내용은 필수입니다.")
        @Size(max = 2000, message = "댓글 내용은 2000자 이하여야 합니다.")
        private String content;
        
        @NotNull(message = "게시글 ID는 필수입니다.")
        @Min(value = 1, message = "유효하지 않은 게시글 ID입니다.")
        private Long postId;
        
        @Min(value = 1, message = "유효하지 않은 부모 댓글 ID입니다.")
        private Long parentId;
        
        private Boolean isSpoiler;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        @Size(max = 2000, message = "댓글 내용은 2000자 이하여야 합니다.")
        private String content;
        
        private Boolean isSpoiler;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentResponse {
        private Long id;
        private String content;
        private UserDto.UserResponse user;
        private Long postId;
        private Long parentId;
        private Integer depth;
        private Boolean isSpoiler;
        private Integer likeCount;
        private Integer dislikeCount;
        private List<CommentResponse> replies;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentSummary {
        private Long id;
        private String content;
        private UserDto.UserResponse user;
        private Integer depth;
        private Boolean isSpoiler;
        private Integer likeCount;
        private Integer replyCount;
        private LocalDateTime createdAt;
    }
}
