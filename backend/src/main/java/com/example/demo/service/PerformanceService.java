package com.example.demo.service;

import com.example.demo.dto.PerformanceDto;
import com.example.demo.entity.Performance;
import com.example.demo.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerformanceService {
    
    private final PerformanceRepository performanceRepository;
    
    @Transactional
    public PerformanceDto.PerformanceResponse createPerformance(PerformanceDto.CreateRequest request) {
        Performance performance = Performance.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .posterImage(request.getPosterImage())
                .genre(request.getGenre())
                .venue(request.getVenue())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .runningTime(request.getRunningTime())
                .status(request.getStatus() != null ? request.getStatus() : Performance.Status.UPCOMING)
                .build();
        
        Performance savedPerformance = performanceRepository.save(performance);
        
        return convertToPerformanceResponse(savedPerformance);
    }
    
    public PerformanceDto.PerformanceResponse getPerformanceById(Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new RuntimeException("공연을 찾을 수 없습니다."));
        
        return convertToPerformanceResponse(performance);
    }
    
    public Page<PerformanceDto.PerformanceSummary> getAllPerformances(Pageable pageable) {
        Page<Performance> performances = performanceRepository.findAll(pageable);
        
        return performances.map(this::convertToPerformanceSummary);
    }
    
    public Page<PerformanceDto.PerformanceSummary> getPerformancesByStatus(Performance.Status status, Pageable pageable) {
        Page<Performance> performances = performanceRepository.findByStatus(status, pageable);
        
        return performances.map(this::convertToPerformanceSummary);
    }
    
    public Page<PerformanceDto.PerformanceSummary> searchPerformances(String keyword, Pageable pageable) {
        Page<Performance> performances = performanceRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        
        return performances.map(this::convertToPerformanceSummary);
    }
    
    public List<PerformanceDto.PerformanceSummary> getUpcomingPerformances() {
        List<Performance> performances = performanceRepository.findUpcomingPerformances(LocalDate.now());
        
        return performances.stream()
                .map(this::convertToPerformanceSummary)
                .toList();
    }
    
    public List<PerformanceDto.PerformanceSummary> getOngoingPerformances() {
        List<Performance> performances = performanceRepository.findOngoingPerformances(LocalDate.now());
        
        return performances.stream()
                .map(this::convertToPerformanceSummary)
                .toList();
    }
    
    public List<PerformanceDto.PerformanceSummary> getCompletedPerformances() {
        List<Performance> performances = performanceRepository.findCompletedPerformances(LocalDate.now());
        
        return performances.stream()
                .map(this::convertToPerformanceSummary)
                .toList();
    }
    
    @Transactional
    public PerformanceDto.PerformanceResponse updatePerformance(Long performanceId, PerformanceDto.UpdateRequest request) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new RuntimeException("공연을 찾을 수 없습니다."));
        
        if (request.getTitle() != null) {
            performance.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            performance.setDescription(request.getDescription());
        }
        if (request.getPosterImage() != null) {
            performance.setPosterImage(request.getPosterImage());
        }
        if (request.getGenre() != null) {
            performance.setGenre(request.getGenre());
        }
        if (request.getVenue() != null) {
            performance.setVenue(request.getVenue());
        }
        if (request.getStartDate() != null) {
            performance.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            performance.setEndDate(request.getEndDate());
        }
        if (request.getRunningTime() != null) {
            performance.setRunningTime(request.getRunningTime());
        }

        if (request.getStatus() != null) {
            performance.setStatus(request.getStatus());
        }
        
        Performance updatedPerformance = performanceRepository.save(performance);
        
        return convertToPerformanceResponse(updatedPerformance);
    }
    
    @Transactional
    public void deletePerformance(Long performanceId) {
        if (!performanceRepository.existsById(performanceId)) {
            throw new RuntimeException("공연을 찾을 수 없습니다.");
        }
        
        performanceRepository.deleteById(performanceId);
    }
    
    private PerformanceDto.PerformanceResponse convertToPerformanceResponse(Performance performance) {
        return PerformanceDto.PerformanceResponse.builder()
                .id(performance.getId())
                .title(performance.getTitle())
                .description(performance.getDescription())
                .posterImage(performance.getPosterImage())
                .genre(performance.getGenre())
                .venue(performance.getVenue())
                .startDate(performance.getStartDate())
                .endDate(performance.getEndDate())
                .runningTime(performance.getRunningTime())

                .status(performance.getStatus())
                .createdAt(performance.getCreatedAt())
                .updatedAt(performance.getUpdatedAt())
                .build();
    }
    
    private PerformanceDto.PerformanceSummary convertToPerformanceSummary(Performance performance) {
        return PerformanceDto.PerformanceSummary.builder()
                .id(performance.getId())
                .title(performance.getTitle())
                .posterImage(performance.getPosterImage())
                .genre(performance.getGenre())
                .venue(performance.getVenue())
                .startDate(performance.getStartDate())
                .endDate(performance.getEndDate())
                .status(performance.getStatus())
                .build();
    }
}
