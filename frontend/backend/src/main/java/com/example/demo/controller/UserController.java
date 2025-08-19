package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @PostMapping("/signup")
    public ResponseEntity<UserDto.UserResponse> signUp(@Valid @RequestBody UserDto.SignUpRequest request) {
        UserDto.UserResponse response = userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto.UserResponse> getUserById(@PathVariable Long userId) {
        UserDto.UserResponse response = userService.getUserById(userId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDto.UserResponse> getUserByUsername(@PathVariable String username) {
        UserDto.UserResponse response = userService.getUserByUsername(username);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{userId}/profile")
    public ResponseEntity<UserDto.UserResponse> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UserDto.UpdateProfileRequest request) {
        UserDto.UserResponse response = userService.updateProfile(userId, request);
        return ResponseEntity.ok(response);
    }
}
