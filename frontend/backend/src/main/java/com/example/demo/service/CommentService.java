package com.example.demo.service;

import com.example.demo.dto.CommentDto;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public CommentDto.CommentResponse createComment(Long userId, CommentDto.CreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        
        Comment parentComment = null;
        if (request.getParentId() != null) {
            parentComment = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("부모 댓글을 찾을 수 없습니다."));
        }
        
        Comment comment = Comment.builder()
                .content(request.getContent())
                .user(user)
                .post(post)
                .parent(parentComment)
                .isSpoiler(request.getIsSpoiler() != null ? request.getIsSpoiler() : false)
                .build();
        
        // 댓글 깊이 설정
        comment.setDepth();
        
        Comment savedComment = commentRepository.save(comment);
        
        return convertToCommentResponse(savedComment);
    }
    
    public List<CommentDto.CommentResponse> getCommentsByPost(Long postId) {
        List<Comment> topLevelComments = commentRepository.findTopLevelCommentsByPost(postId);
        
        return topLevelComments.stream()
                .map(this::convertToCommentResponseWithReplies)
                .collect(Collectors.toList());
    }
    
    public Page<CommentDto.CommentSummary> getCommentsByUser(Long userId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByUserId(userId, pageable);
        
        return comments.map(this::convertToCommentSummary);
    }
    
    public List<CommentDto.CommentResponse> getTopCommentsByPost(Long postId) {
        List<Comment> comments = commentRepository.findTopCommentsByPost(postId);
        
        return comments.stream()
                .map(this::convertToCommentResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public CommentDto.CommentResponse updateComment(Long commentId, Long userId, CommentDto.UpdateRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        
        // 작성자만 수정 가능
        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("댓글을 수정할 권한이 없습니다.");
        }
        
        if (request.getContent() != null) {
            comment.setContent(request.getContent());
        }
        if (request.getIsSpoiler() != null) {
            comment.setIsSpoiler(request.getIsSpoiler());
        }
        
        Comment updatedComment = commentRepository.save(comment);
        
        return convertToCommentResponse(updatedComment);
    }
    
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        
        // 작성자만 삭제 가능
        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("댓글을 삭제할 권한이 없습니다.");
        }
        
        commentRepository.deleteById(commentId);
    }
    
    private CommentDto.CommentResponse convertToCommentResponse(Comment comment) {
        return CommentDto.CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .user(convertToUserResponse(comment.getUser()))
                .postId(comment.getPost().getId())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .depth(comment.getDepth())
                .isSpoiler(comment.getIsSpoiler())
                .likeCount(comment.getLikeCount())
                .dislikeCount(comment.getDislikeCount())
                .replies(null) // 단일 댓글 응답에서는 replies는 null
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
    
    private CommentDto.CommentResponse convertToCommentResponseWithReplies(Comment comment) {
        List<CommentDto.CommentResponse> replies = comment.getReplies().stream()
                .map(this::convertToCommentResponse)
                .collect(Collectors.toList());
        
        return CommentDto.CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .user(convertToUserResponse(comment.getUser()))
                .postId(comment.getPost().getId())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .depth(comment.getDepth())
                .isSpoiler(comment.getIsSpoiler())
                .likeCount(comment.getLikeCount())
                .dislikeCount(comment.getDislikeCount())
                .replies(replies)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
    
    private CommentDto.CommentSummary convertToCommentSummary(Comment comment) {
        return CommentDto.CommentSummary.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .user(convertToUserResponse(comment.getUser()))
                .depth(comment.getDepth())
                .isSpoiler(comment.getIsSpoiler())
                .likeCount(comment.getLikeCount())
                .replyCount(comment.getReplies().size())
                .createdAt(comment.getCreatedAt())
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
