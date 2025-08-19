package com.example.demo.controller;

import com.example.demo.dto.PostDto;
import com.example.demo.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    
    private final PostService postService;
    
    @PostMapping
    public ResponseEntity<PostDto.PostResponse> createPost(
            @RequestParam Long userId,
            @Valid @RequestBody PostDto.CreateRequest request) {
        PostDto.PostResponse response = postService.createPost(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{postId}")
    public ResponseEntity<PostDto.PostResponse> getPostById(@PathVariable Long postId) {
        PostDto.PostResponse response = postService.getPostById(postId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/performance/{performanceId}")
    public ResponseEntity<Page<PostDto.PostSummary>> getPostsByPerformance(
            @PathVariable Long performanceId,
            Pageable pageable) {
        Page<PostDto.PostSummary> response = postService.getPostsByPerformance(performanceId, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/performance/{performanceId}/type/{postType}")
    public ResponseEntity<Page<PostDto.PostSummary>> getPostsByPerformanceAndType(
            @PathVariable Long performanceId,
            @PathVariable String postType,
            Pageable pageable) {
        try {
            var postTypeEnum = com.example.demo.entity.Post.PostType.valueOf(postType.toUpperCase());
            Page<PostDto.PostSummary> response = postService.getPostsByPerformanceAndType(performanceId, postTypeEnum, pageable);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/performance/{performanceId}/spoiler/{isSpoiler}")
    public ResponseEntity<Page<PostDto.PostSummary>> getPostsByPerformanceAndSpoiler(
            @PathVariable Long performanceId,
            @PathVariable Boolean isSpoiler,
            Pageable pageable) {
        Page<PostDto.PostSummary> response = postService.getPostsByPerformanceAndSpoiler(performanceId, isSpoiler, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/performance/{performanceId}/latest")
    public ResponseEntity<List<PostDto.PostSummary>> getLatestPostsByPerformance(@PathVariable Long performanceId) {
        List<PostDto.PostSummary> response = postService.getLatestPostsByPerformance(performanceId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/performance/{performanceId}/top")
    public ResponseEntity<List<PostDto.PostSummary>> getTopPostsByPerformance(@PathVariable Long performanceId) {
        List<PostDto.PostSummary> response = postService.getTopPostsByPerformance(performanceId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<PostDto.PostSummary>> getPostsByUser(
            @PathVariable Long userId,
            Pageable pageable) {
        Page<PostDto.PostSummary> response = postService.getPostsByUser(userId, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<PostDto.PostSummary>> searchPosts(
            @RequestParam String keyword,
            Pageable pageable) {
        Page<PostDto.PostSummary> response = postService.searchPosts(keyword, pageable);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{postId}")
    public ResponseEntity<PostDto.PostResponse> updatePost(
            @PathVariable Long postId,
            @RequestParam Long userId,
            @Valid @RequestBody PostDto.UpdateRequest request) {
        PostDto.PostResponse response = postService.updatePost(postId, userId, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @RequestParam Long userId) {
        postService.deletePost(postId, userId);
        return ResponseEntity.noContent().build();
    }
}
