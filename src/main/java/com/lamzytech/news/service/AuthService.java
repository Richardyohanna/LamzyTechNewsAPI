package com.lamzytech.news.service;

import com.lamzytech.news.dto.AuthResponse;
import com.lamzytech.news.dto.LoginRequest;
import com.lamzytech.news.dto.RegisterRequest;
import com.lamzytech.news.dto.StudentResponse;
import com.lamzytech.news.entity.Role;
import com.lamzytech.news.entity.Student;
import com.lamzytech.news.exception.DuplicateResourceException;
import com.lamzytech.news.repository.StudentRepository;
import com.lamzytech.news.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("A student with this email already exists");
        }

        Student student = Student.builder()
                .fullName(request.getFullName())
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .department(request.getDepartment())
                .level(request.getLevel())
                .role(Role.STUDENT)
                .build();

        Student saved = studentRepository.save(student);
        String token = jwtService.generateToken(saved);

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .student(StudentResponse.fromEntity(saved))
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail().toLowerCase(),
                        request.getPassword()
                )
        );

        Student student = studentRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new IllegalStateException("Student not found after authentication"));

        String token = jwtService.generateToken(student);

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .student(StudentResponse.fromEntity(student))
                .build();
    }
}
