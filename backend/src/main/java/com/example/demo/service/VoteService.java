package com.example.demo.service;

import com.example.demo.dto.VoteDto;
import com.example.demo.entity.Vote;
import com.example.demo.entity.Post;
import com.example.demo.entity.Comment;
import com.example.demo.entity.User;
import com.example.demo.repository.VoteRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteService {
    
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public VoteDto.VoteResponse vote(VoteDto.VoteRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        // 기존 투표가 있는지 확인
        Vote existingVote = null;
        if (request.getPostId() != null) {
            existingVote = voteRepository.findByUserIdAndPostId(userId, request.getPostId()).orElse(null);
        } else if (request.getCommentId() != null) {
            existingVote = voteRepository.findByUserIdAndCommentId(userId, request.getCommentId()).orElse(null);
        }
        
        // 기존 투표가 있으면 삭제
        if (existingVote != null) {
            voteRepository.delete(existingVote);
            
            // 같은 타입으로 다시 투표하면 취소
            if (existingVote.getVoteType() == request.getVoteType()) {
                return null; // 투표 취소
            }
        }
        
        // 새 투표 생성
        Vote vote = Vote.builder()
                .user(user)
                .post(request.getPostId() != null ? postRepository.getReferenceById(request.getPostId()) : null)
                .comment(request.getCommentId() != null ? commentRepository.getReferenceById(request.getCommentId()) : null)
                .voteType(request.getVoteType())
                .build();
        
        Vote savedVote = voteRepository.save(vote);
        
        return convertToVoteResponse(savedVote);
    }
    
    public VoteDto.VoteSummary getVoteSummary(Long postId, Long commentId, Long userId) {
        Long likeCount = 0L;
        Long dislikeCount = 0L;
        Long agreeCount = 0L;
        Long disagreeCount = 0L;
        Vote.VoteType userVote = null;
        
        if (postId != null) {
            likeCount = voteRepository.countByPostIdAndVoteType(postId, Vote.VoteType.LIKE);
            dislikeCount = voteRepository.countByPostIdAndVoteType(postId, Vote.VoteType.DISLIKE);
            agreeCount = voteRepository.countByPostIdAndVoteType(postId, Vote.VoteType.AGREE);
            disagreeCount = voteRepository.countByPostIdAndVoteType(postId, Vote.VoteType.DISAGREE);
            
            if (userId != null) {
                var optionalVote = voteRepository.findByUserIdAndPostId(userId, postId);
                if (optionalVote.isPresent()) {
                    userVote = optionalVote.get().getVoteType();
                }
            }
        } else if (commentId != null) {
            likeCount = voteRepository.countByCommentIdAndVoteType(commentId, Vote.VoteType.LIKE);
            dislikeCount = voteRepository.countByCommentIdAndVoteType(commentId, Vote.VoteType.DISLIKE);
            agreeCount = voteRepository.countByCommentIdAndVoteType(commentId, Vote.VoteType.AGREE);
            disagreeCount = voteRepository.countByCommentIdAndVoteType(commentId, Vote.VoteType.DISAGREE);
            
            if (userId != null) {
                var optionalVote = voteRepository.findByUserIdAndCommentId(userId, commentId);
                if (optionalVote.isPresent()) {
                    userVote = optionalVote.get().getVoteType();
                }
            }
        }
        
        return VoteDto.VoteSummary.builder()
                .postId(postId)
                .commentId(commentId)
                .likeCount(likeCount)
                .dislikeCount(dislikeCount)
                .agreeCount(agreeCount)
                .disagreeCount(disagreeCount)
                .userVote(userVote)
                .build();
    }
    
    public boolean hasUserVoted(Long userId, Long postId, Long commentId) {
        if (postId != null) {
            return voteRepository.existsByUserIdAndPostId(userId, postId);
        } else if (commentId != null) {
            return voteRepository.existsByUserIdAndCommentId(userId, commentId);
        }
        return false;
    }
    
    @Transactional
    public void removeVote(Long userId, Long postId, Long commentId) {
        if (postId != null) {
            voteRepository.deleteByUserIdAndPostId(userId, postId);
        } else if (commentId != null) {
            voteRepository.deleteByUserIdAndCommentId(userId, commentId);
        }
    }
    
    private VoteDto.VoteResponse convertToVoteResponse(Vote vote) {
        return VoteDto.VoteResponse.builder()
                .id(vote.getId())
                .user(convertToUserResponse(vote.getUser()))
                .postId(vote.getPost() != null ? vote.getPost().getId() : null)
                .commentId(vote.getComment() != null ? vote.getComment().getId() : null)
                .voteType(vote.getVoteType())
                .createdAt(vote.getCreatedAt())
                .build();
    }
    
    private com.example.demo.dto.UserDto.UserResponse convertToUserResponse(User user) {
        return com.example.demo.dto.UserDto.UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
