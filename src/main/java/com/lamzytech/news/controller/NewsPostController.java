package com.lamzytech.news.controller;

import com.lamzytech.news.dto.ApiResponse;
import com.lamzytech.news.dto.NewsPostRequest;
import com.lamzytech.news.dto.NewsPostResponse;
import com.lamzytech.news.entity.Student;
import com.lamzytech.news.service.NewsPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class NewsPostController {

    private final NewsPostService newsPostService;

    // Public - anyone can view all posts (paginated)
    @GetMapping
    public ResponseEntity<ApiResponse<Page<NewsPostResponse>>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<NewsPostResponse> posts = newsPostService.getAllPosts(pageable);
        return ResponseEntity.ok(ApiResponse.success("Posts fetched successfully", posts));
    }

    // Public - anyone can view a single post
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NewsPostResponse>> getPostById(@PathVariable Long id) {
        NewsPostResponse post = newsPostService.getPostById(id);
        return ResponseEntity.ok(ApiResponse.success("Post fetched successfully", post));
    }

    // Protected - only authenticated students can create
    @PostMapping
    public ResponseEntity<ApiResponse<NewsPostResponse>> createPost(
            @Valid @RequestBody NewsPostRequest request,
            @AuthenticationPrincipal Student currentStudent) {
        NewsPostResponse response = newsPostService.createPost(request, currentStudent.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Post created successfully", response));
    }

    // Protected - only the author can update
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NewsPostResponse>> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody NewsPostRequest request,
            @AuthenticationPrincipal Student currentStudent) {
        NewsPostResponse response = newsPostService.updatePost(id, request, currentStudent.getEmail());
        return ResponseEntity.ok(ApiResponse.success("Post updated successfully", response));
    }

    // Protected - only the author can delete
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal Student currentStudent) {
        newsPostService.deletePost(id, currentStudent.getEmail());
        return ResponseEntity.ok(ApiResponse.success("Post deleted successfully", null));
    }
}
