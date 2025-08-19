package com.example.demo.dto;

import com.example.demo.entity.Post;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class PostDto {
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "게시글 제목은 필수입니다.")
        @Size(max = 200, message = "게시글 제목은 200자 이하여야 합니다.")
        private String title;
        
        @NotBlank(message = "게시글 내용은 필수입니다.")
        @Size(max = 10000, message = "게시글 내용은 10000자 이하여야 합니다.")
        private String content;
        
        @NotNull(message = "공연 ID는 필수입니다.")
        @Min(value = 1, message = "유효하지 않은 공연 ID입니다.")
        private Long performanceId;
        
        private Post.PostType postType;
        
        private Boolean isSpoiler;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        @Size(max = 200, message = "게시글 제목은 200자 이하여야 합니다.")
        private String title;
        
        @Size(max = 10000, message = "게시글 내용은 10000자 이하여야 합니다.")
        private String content;
        
        private Post.PostType postType;
        
        private Boolean isSpoiler;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostResponse {
        private Long id;
        private String title;
        private String content;
        private UserDto.UserResponse user;
        private PerformanceDto.PerformanceSummary performance;
        private Post.PostType postType;
        private Boolean isSpoiler;
        private Integer viewCount;
        private Integer likeCount;
        private Integer dislikeCount;
        private Integer commentCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostSummary {
        private Long id;
        private String title;
        private String content;
        private UserDto.UserResponse user;
        private Post.PostType postType;
        private Boolean isSpoiler;
        private Integer viewCount;
        private Integer likeCount;
        private Integer commentCount;
        private LocalDateTime createdAt;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostDetailResponse {
        private PostResponse post;
        private List<CommentDto.CommentResponse> comments;
    }
}
