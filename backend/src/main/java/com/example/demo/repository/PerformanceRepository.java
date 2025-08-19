package com.example.demo.repository;

import com.example.demo.entity.Performance;
import com.example.demo.entity.Performance.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    
    Page<Performance> findByStatus(Status status, Pageable pageable);
    
    Page<Performance> findByGenreContainingIgnoreCase(String genre, Pageable pageable);
    
    Page<Performance> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    @Query("SELECT p FROM Performance p WHERE p.startDate >= :today ORDER BY p.startDate ASC")
    List<Performance> findUpcomingPerformances(@Param("today") LocalDate today);
    
    @Query("SELECT p FROM Performance p WHERE p.endDate < :today ORDER BY p.endDate DESC")
    List<Performance> findCompletedPerformances(@Param("today") LocalDate today);
    
    @Query("SELECT p FROM Performance p WHERE p.startDate <= :today AND p.endDate >= :today")
    List<Performance> findOngoingPerformances(@Param("today") LocalDate today);
}
