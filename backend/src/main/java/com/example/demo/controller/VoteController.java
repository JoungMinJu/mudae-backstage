package com.example.demo.controller;

import com.example.demo.dto.VoteDto;
import com.example.demo.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class VoteController {
    
    private final VoteService voteService;
    
    @PostMapping
    public ResponseEntity<VoteDto.VoteResponse> vote(
            @RequestParam Long userId,
            @Valid @RequestBody VoteDto.VoteRequest request) {
        VoteDto.VoteResponse response = voteService.vote(request, userId);
        
        // 투표 취소인 경우
        if (response == null) {
            return ResponseEntity.ok().build();
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/summary")
    public ResponseEntity<VoteDto.VoteSummary> getVoteSummary(
            @RequestParam(required = false) Long postId,
            @RequestParam(required = false) Long commentId,
            @RequestParam(required = false) Long userId) {
        VoteDto.VoteSummary response = voteService.getVoteSummary(postId, commentId, userId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/check")
    public ResponseEntity<Boolean> hasUserVoted(
            @RequestParam Long userId,
            @RequestParam(required = false) Long postId,
            @RequestParam(required = false) Long commentId) {
        boolean hasVoted = voteService.hasUserVoted(userId, postId, commentId);
        return ResponseEntity.ok(hasVoted);
    }
    
    @DeleteMapping
    public ResponseEntity<Void> removeVote(
            @RequestParam Long userId,
            @RequestParam(required = false) Long postId,
            @RequestParam(required = false) Long commentId) {
        voteService.removeVote(userId, postId, commentId);
        return ResponseEntity.noContent().build();
    }
}
