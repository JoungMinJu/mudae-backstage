package com.example.demo.dto;

import com.example.demo.entity.Performance;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PerformanceDto {
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "공연 제목은 필수입니다.")
        @Size(max = 200, message = "공연 제목은 200자 이하여야 합니다.")
        private String title;
        
        @Size(max = 2000, message = "공연 설명은 2000자 이하여야 합니다.")
        private String description;
        
        @Size(max = 500, message = "포스터 이미지 URL은 500자 이하여야 합니다.")
        private String posterImage;
        
        @NotBlank(message = "장르는 필수입니다.")
        @Size(max = 100, message = "장르는 100자 이하여야 합니다.")
        private String genre;
        
        @NotBlank(message = "공연장은 필수입니다.")
        @Size(max = 200, message = "공연장은 200자 이하여야 합니다.")
        private String venue;
        
        @NotNull(message = "공연 시작일은 필수입니다.")
        private LocalDate startDate;
        
        @NotNull(message = "공연 종료일은 필수입니다.")
        private LocalDate endDate;
        
        @Min(value = 1, message = "공연 시간은 1분 이상이어야 합니다.")
        @Max(value = 1440, message = "공연 시간은 24시간(1440분) 이하여야 합니다.")
        private Integer runningTime;
        
        @NotNull(message = "공연 상태는 필수입니다.")
        private Performance.Status status;
        
        // 공연 종료일이 시작일보다 늦어야 함
        @AssertTrue(message = "공연 종료일은 시작일보다 늦어야 합니다.")
        public boolean isEndDateAfterStartDate() {
            if (startDate == null || endDate == null) {
                return true; // null 체크는 @NotNull에서 처리
            }
            return endDate.isAfter(startDate) || endDate.isEqual(startDate);
        }
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        @Size(max = 200, message = "공연 제목은 200자 이하여야 합니다.")
        private String title;
        
        @Size(max = 2000, message = "공연 설명은 2000자 이하여야 합니다.")
        private String description;
        
        @Size(max = 500, message = "포스터 이미지 URL은 500자 이하여야 합니다.")
        private String posterImage;
        
        @Size(max = 100, message = "장르는 100자 이하여야 합니다.")
        private String genre;
        
        @Size(max = 200, message = "공연장은 200자 이하여야 합니다.")
        private String venue;
        
        private LocalDate startDate;
        
        private LocalDate endDate;
        
        @Min(value = 1, message = "공연 시간은 1분 이상이어야 합니다.")
        @Max(value = 1440, message = "공연 시간은 24시간(1440분) 이하여야 합니다.")
        private Integer runningTime;
        
        private Performance.Status status;
        
        // 공연 종료일이 시작일보다 늦어야 함
        @AssertTrue(message = "공연 종료일은 시작일보다 늦어야 합니다.")
        public boolean isEndDateAfterStartDate() {
            if (startDate == null || endDate == null) {
                return true; // null 체크는 @NotNull에서 처리
            }
            return endDate.isAfter(startDate) || endDate.isEqual(startDate);
        }
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceResponse {
        private Long id;
        private String title;
        private String description;
        private String posterImage;
        private String genre;
        private String venue;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer runningTime;
        private Performance.Status status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceSummary {
        private Long id;
        private String title;
        private String posterImage;
        private String genre;
        private String venue;
        private LocalDate startDate;
        private LocalDate endDate;
        private Performance.Status status;
    }
}
