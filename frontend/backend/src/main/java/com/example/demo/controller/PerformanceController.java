package com.example.demo.controller;

import com.example.demo.dto.PerformanceDto;
import com.example.demo.service.PerformanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/performances")
@RequiredArgsConstructor
public class PerformanceController {
    
    private final PerformanceService performanceService;
    
    @PostMapping
    public ResponseEntity<PerformanceDto.PerformanceResponse> createPerformance(
            @Valid @RequestBody PerformanceDto.CreateRequest request) {
        PerformanceDto.PerformanceResponse response = performanceService.createPerformance(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{performanceId}")
    public ResponseEntity<PerformanceDto.PerformanceResponse> getPerformanceById(@PathVariable Long performanceId) {
        PerformanceDto.PerformanceResponse response = performanceService.getPerformanceById(performanceId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<Page<PerformanceDto.PerformanceSummary>> getAllPerformances(Pageable pageable) {
        Page<PerformanceDto.PerformanceSummary> response = performanceService.getAllPerformances(pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<PerformanceDto.PerformanceSummary>> getPerformancesByStatus(
            @PathVariable String status,
            Pageable pageable) {
        try {
            var statusEnum = com.example.demo.entity.Performance.Status.valueOf(status.toUpperCase());
            Page<PerformanceDto.PerformanceSummary> response = performanceService.getPerformancesByStatus(statusEnum, pageable);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<PerformanceDto.PerformanceSummary>> searchPerformances(
            @RequestParam String keyword,
            Pageable pageable) {
        Page<PerformanceDto.PerformanceSummary> response = performanceService.searchPerformances(keyword, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/upcoming")
    public ResponseEntity<List<PerformanceDto.PerformanceSummary>> getUpcomingPerformances() {
        List<PerformanceDto.PerformanceSummary> response = performanceService.getUpcomingPerformances();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/ongoing")
    public ResponseEntity<List<PerformanceDto.PerformanceSummary>> getOngoingPerformances() {
        List<PerformanceDto.PerformanceSummary> response = performanceService.getOngoingPerformances();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/completed")
    public ResponseEntity<List<PerformanceDto.PerformanceSummary>> getCompletedPerformances() {
        List<PerformanceDto.PerformanceSummary> response = performanceService.getCompletedPerformances();
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{performanceId}")
    public ResponseEntity<PerformanceDto.PerformanceResponse> updatePerformance(
            @PathVariable Long performanceId,
            @Valid @RequestBody PerformanceDto.UpdateRequest request) {
        PerformanceDto.PerformanceResponse response = performanceService.updatePerformance(performanceId, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{performanceId}")
    public ResponseEntity<Void> deletePerformance(@PathVariable Long performanceId) {
        performanceService.deletePerformance(performanceId);
        return ResponseEntity.noContent().build();
    }
}
