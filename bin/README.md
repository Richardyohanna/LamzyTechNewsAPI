# LamzyTech Student News API

A RESTful News API for Nigerian-style student campus news, built with **Java 17**, **Spring Boot 3**, **Spring Security**, **JWT (jjwt)**, **Spring Data JPA**, and **MySQL**.

## Features

- Student registration & login with BCrypt password hashing
- JWT-based stateless authentication
- Public read access to news posts; authenticated write access
- Ownership enforcement — only a post's author can update/delete it
- Centralized exception handling with a consistent JSON response envelope
- Bean Validation on all request DTOs

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.3.4 |
| Security | Spring Security 6 + JWT (jjwt 0.12.5) |
| Persistence | Spring Data JPA (Hibernate) |
| Database | MySQL 8 |
| Build tool | Maven |
| Boilerplate | Lombok |

## Project Structure

```
src/main/java/com/lamzytech/news/
├── config/          # SecurityConfig, ApplicationConfig (beans)
├── controller/      # AuthController, StudentController, NewsPostController
├── dto/             # Request/response DTOs incl. ApiResponse envelope
├── entity/          # Student, NewsPost, Role
├── repository/      # StudentRepository, NewsPostRepository
├── security/        # JwtService, JwtAuthenticationFilter, CustomUserDetailsService,
│                     # JwtAuthEntryPoint, JwtAccessDeniedHandler
├── service/         # AuthService, StudentService, NewsPostService
├── exception/        # Custom exceptions + GlobalExceptionHandler
└── NewsApplication.java
```

## Getting Started

### 1. Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8 running locally (or reachable)

### 2. Configure the database

Create a MySQL user/password (or use root) and update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/lamzytech_news?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
```

With `spring.jpa.hibernate.ddl-auto=update`, Hibernate will create the `students` and `news_posts` tables automatically on first run — you do **not** need to run `schema.sql` manually. It's included for reference/manual setup if you prefer.

### 3. Set a real JWT secret

For anything beyond local testing, replace `app.jwt.secret` in `application.properties` with a long, random, securely-stored value (or better, an environment variable):

```properties
app.jwt.secret=${JWT_SECRET:ChangeThisSecretKeyToSomethingLongAndRandomForProductionUse123456}
```

### 4. Run the app

```bash
mvn spring-boot:run
```

The API starts on `http://localhost:8080`.

### 5. Import the Postman collection

Import `postman/LamzyTech-News-API.postman_collection.json` into Postman. The **Register** and **Login** requests automatically save the returned JWT into the `token` collection variable, which subsequent requests reuse via `Authorization: Bearer {{token}}`.

## Response Envelope

Every endpoint returns:

```json
{
  "success": true,
  "message": "Request successful",
  "data": {}
}
```

Errors follow the same shape with `"success": false` and `"data": null` (or a map of field errors for validation failures).

## Endpoints

### Auth (public)

| Method | Endpoint | Body |
|---|---|---|
| POST | `/api/auth/register` | `fullName, email, password, department, level` |
| POST | `/api/auth/login` | `email, password` |

### Student (protected)

| Method | Endpoint |
|---|---|
| GET | `/api/students/me` |

### Posts

| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/posts?page=0&size=10` | Public |
| GET | `/api/posts/{id}` | Public |
| POST | `/api/posts` | Authenticated |
| PUT | `/api/posts/{id}` | Author only |
| DELETE | `/api/posts/{id}` | Author only |

### Authentication header

```
Authorization: Bearer <token>
```

## Validation Rules

- `email` — must be a valid email address
- `password` — minimum 6 characters
- `title`, `content`, `category` — required, non-blank

## Notes on Design Decisions

- **Stateless JWT auth**: `SessionCreationPolicy.STATELESS` — no server-side session state.
- **Ownership check**: enforced in `NewsPostService` (not just the controller), comparing the authenticated user's email against `post.author.email`, throwing a `403 Forbidden` (`ForbiddenActionException`) on mismatch.
- **Password never serialized**: `Student.password` is annotated `@JsonIgnore` so it can never leak in a response, even by accident.
- **Pagination on `GET /api/posts`**: defaults to `page=0&size=10`, newest first.
