package com.lamzytech.news.repository;

import com.lamzytech.news.entity.NewsPost;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NewsPostRepository extends JpaRepository<NewsPost, Long> {

    @EntityGraph(attributePaths = {"author"})
    Page<NewsPost> findAllByOrderByCreatedAtDesc(Pageable pageable);
}