package com.lamzytech.news.service;

import com.lamzytech.news.dto.NewsPostRequest;
import com.lamzytech.news.dto.NewsPostResponse;
import com.lamzytech.news.entity.NewsPost;
import com.lamzytech.news.entity.Student;
import com.lamzytech.news.exception.ForbiddenActionException;
import com.lamzytech.news.exception.ResourceNotFoundException;
import com.lamzytech.news.repository.NewsPostRepository;
import com.lamzytech.news.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewsPostService {

    private final NewsPostRepository newsPostRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public NewsPostResponse createPost(NewsPostRequest request, String authorEmail) {
        Student author = studentRepository.findByEmail(authorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        NewsPost post = NewsPost.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .imageUrl(request.getImageUrl())
                .author(author)
                .build();

        NewsPost saved = newsPostRepository.save(post);
        return NewsPostResponse.fromEntity(saved);
    }

    public Page<NewsPostResponse> getAllPosts(Pageable pageable) {
        return newsPostRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(NewsPostResponse::fromEntity);
    }

    public NewsPostResponse getPostById(Long id) {
        NewsPost post = findPostOrThrow(id);
        return NewsPostResponse.fromEntity(post);
    }

    @Transactional
    public NewsPostResponse updatePost(Long id, NewsPostRequest request, String requesterEmail) {
        NewsPost post = findPostOrThrow(id);
        assertIsOwner(post, requesterEmail);

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCategory(request.getCategory());
        post.setImageUrl(request.getImageUrl());

        NewsPost updated = newsPostRepository.save(post);
        return NewsPostResponse.fromEntity(updated);
    }

    @Transactional
    public void deletePost(Long id, String requesterEmail) {
        NewsPost post = findPostOrThrow(id);
        assertIsOwner(post, requesterEmail);
        newsPostRepository.delete(post);
    }

    private NewsPost findPostOrThrow(Long id) {
        return newsPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News post not found with id: " + id));
    }

    private void assertIsOwner(NewsPost post, String requesterEmail) {
        if (!post.getAuthor().getEmail().equalsIgnoreCase(requesterEmail)) {
            throw new ForbiddenActionException("You are not allowed to modify a post you did not author");
        }
    }
}
