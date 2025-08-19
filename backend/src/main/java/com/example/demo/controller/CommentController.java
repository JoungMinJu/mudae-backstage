package com.example.demo.controller;

import com.example.demo.dto.CommentDto;
import com.example.demo.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;
    
    @PostMapping
    public ResponseEntity<CommentDto.CommentResponse> createComment(
            @RequestParam Long userId,
            @Valid @RequestBody CommentDto.CreateRequest request) {
        CommentDto.CommentResponse response = commentService.createComment(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDto.CommentResponse>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentDto.CommentResponse> response = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<CommentDto.CommentSummary>> getCommentsByUser(
            @PathVariable Long userId,
            Pageable pageable) {
        Page<CommentDto.CommentSummary> response = commentService.getCommentsByUser(userId, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/post/{postId}/top")
    public ResponseEntity<List<CommentDto.CommentResponse>> getTopCommentsByPost(@PathVariable Long postId) {
        List<CommentDto.CommentResponse> response = commentService.getTopCommentsByPost(postId);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto.CommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestParam Long userId,
            @Valid @RequestBody CommentDto.UpdateRequest request) {
        CommentDto.CommentResponse response = commentService.updateComment(commentId, userId, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @RequestParam Long userId) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }
}
