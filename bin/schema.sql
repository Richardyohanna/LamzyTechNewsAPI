-- =====================================================
-- LamzyTech Student News API - MySQL Database Schema
-- =====================================================
-- Note: With spring.jpa.hibernate.ddl-auto=update, Hibernate will
-- auto-create/update these tables on application startup. This script
-- is provided for manual setup, reference, or production deployment
-- where you prefer to manage schema migrations yourself.

CREATE DATABASE IF NOT EXISTS lamzytech_news
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE lamzytech_news;

-- =====================================================
-- Table: students
-- =====================================================
CREATE TABLE IF NOT EXISTS students (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name    VARCHAR(255)        NOT NULL,
    email        VARCHAR(255)        NOT NULL UNIQUE,
    password     VARCHAR(255)        NOT NULL,
    department   VARCHAR(255)        NOT NULL,
    level        VARCHAR(50)         NOT NULL,
    role         VARCHAR(20)         NOT NULL DEFAULT 'STUDENT',
    created_at   DATETIME            NOT NULL,

    INDEX idx_students_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Table: news_posts
-- =====================================================
CREATE TABLE IF NOT EXISTS news_posts (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(255)        NOT NULL,
    content      TEXT                NOT NULL,
    category     VARCHAR(100)        NOT NULL,
    image_url    VARCHAR(500),
    created_at   DATETIME            NOT NULL,
    updated_at   DATETIME,
    author_id    BIGINT              NOT NULL,

    CONSTRAINT fk_news_posts_author
        FOREIGN KEY (author_id) REFERENCES students(id)
        ON DELETE CASCADE,

    INDEX idx_news_posts_author_id (author_id),
    INDEX idx_news_posts_category (category),
    INDEX idx_news_posts_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
