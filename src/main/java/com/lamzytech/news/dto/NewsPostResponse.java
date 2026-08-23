package com.lamzytech.news.dto;

import com.lamzytech.news.entity.NewsPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsPostResponse {

    private Long id;
    private String title;
    private String content;
    private String category;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AuthorSummary author;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AuthorSummary {
        private Long id;
        private String fullName;
        private String department;
    }

    public static NewsPostResponse fromEntity(NewsPost post) {
        return NewsPostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .imageUrl(post.getImageUrl())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .author(AuthorSummary.builder()
                        .id(post.getAuthor().getId())
                        .fullName(post.getAuthor().getFullName())
                        .department(post.getAuthor().getDepartment())
                        .build())
                .build();
    }
}
