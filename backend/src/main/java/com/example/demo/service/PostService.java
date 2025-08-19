package com.example.demo.service;

import com.example.demo.dto.PostDto;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.entity.Performance;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PerformanceRepository performanceRepository;
    
    @Transactional
    public PostDto.PostResponse createPost(Long userId, PostDto.CreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        Performance performance = performanceRepository.findById(request.getPerformanceId())
                .orElseThrow(() -> new RuntimeException("공연을 찾을 수 없습니다."));
        
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .performance(performance)
                .postType(request.getPostType() != null ? request.getPostType() : Post.PostType.GENERAL)
                .isSpoiler(request.getIsSpoiler() != null ? request.getIsSpoiler() : false)
                .build();
        
        Post savedPost = postRepository.save(post);
        
        return convertToPostResponse(savedPost);
    }
    
    public PostDto.PostResponse getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        
        // 조회수 증가
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
        
        return convertToPostResponse(post);
    }
    
    public Page<PostDto.PostSummary> getPostsByPerformance(Long performanceId, Pageable pageable) {
        Page<Post> posts = postRepository.findByPerformanceId(performanceId, pageable);
        
        return posts.map(this::convertToPostSummary);
    }
    
    public Page<PostDto.PostSummary> getPostsByPerformanceAndType(Long performanceId, Post.PostType postType, Pageable pageable) {
        Page<Post> posts = postRepository.findByPerformanceIdAndPostType(performanceId, postType, pageable);
        
        return posts.map(this::convertToPostSummary);
    }
    
    public Page<PostDto.PostSummary> getPostsByPerformanceAndSpoiler(Long performanceId, Boolean isSpoiler, Pageable pageable) {
        Page<Post> posts = postRepository.findByPerformanceIdAndIsSpoiler(performanceId, isSpoiler, pageable);
        
        return posts.map(this::convertToPostSummary);
    }
    
    public List<PostDto.PostSummary> getLatestPostsByPerformance(Long performanceId) {
        List<Post> posts = postRepository.findLatestPostsByPerformance(performanceId);
        
        return posts.stream()
                .map(this::convertToPostSummary)
                .toList();
    }
    
    public List<PostDto.PostSummary> getTopPostsByPerformance(Long performanceId) {
        List<Post> posts = postRepository.findTopPostsByPerformance(performanceId);
        
        return posts.stream()
                .map(this::convertToPostSummary)
                .toList();
    }
    
    public Page<PostDto.PostSummary> getPostsByUser(Long userId, Pageable pageable) {
        Page<Post> posts = postRepository.findByUserId(userId, pageable);
        
        return posts.map(this::convertToPostSummary);
    }
    
    public Page<PostDto.PostSummary> searchPosts(String keyword, Pageable pageable) {
        Page<Post> posts = postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
                keyword, keyword, pageable);
        
        return posts.map(this::convertToPostSummary);
    }
    
    @Transactional
    public PostDto.PostResponse updatePost(Long postId, Long userId, PostDto.UpdateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        
        // 작성자만 수정 가능
        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("게시글을 수정할 권한이 없습니다.");
        }
        
        if (request.getTitle() != null) {
            post.setTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            post.setContent(request.getContent());
        }
        if (request.getPostType() != null) {
            post.setPostType(request.getPostType());
        }
        if (request.getIsSpoiler() != null) {
            post.setIsSpoiler(request.getIsSpoiler());
        }
        
        Post updatedPost = postRepository.save(post);
        
        return convertToPostResponse(updatedPost);
    }
    
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        
        // 작성자만 삭제 가능
        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("게시글을 삭제할 권한이 없습니다.");
        }
        
        postRepository.deleteById(postId);
    }
    
    private PostDto.PostResponse convertToPostResponse(Post post) {
        return PostDto.PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .user(convertToUserResponse(post.getUser()))
                .performance(convertToPerformanceSummary(post.getPerformance()))
                .postType(post.getPostType())
                .isSpoiler(post.getIsSpoiler())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .dislikeCount(post.getDislikeCount())
                .commentCount(post.getComments().size())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
    
    private PostDto.PostSummary convertToPostSummary(Post post) {
        return PostDto.PostSummary.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .user(convertToUserResponse(post.getUser()))
                .postType(post.getPostType())
                .isSpoiler(post.getIsSpoiler())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .commentCount(post.getComments().size())
                .createdAt(post.getCreatedAt())
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
    
    private com.example.demo.dto.PerformanceDto.PerformanceSummary convertToPerformanceSummary(Performance performance) {
        return com.example.demo.dto.PerformanceDto.PerformanceSummary.builder()
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
