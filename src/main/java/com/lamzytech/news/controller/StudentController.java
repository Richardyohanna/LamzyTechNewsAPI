package com.lamzytech.news.controller;

import com.lamzytech.news.dto.ApiResponse;
import com.lamzytech.news.dto.StudentResponse;
import com.lamzytech.news.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lamzytech.news.entity.Student;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<StudentResponse>> getMyProfile(@AuthenticationPrincipal Student currentStudent) {
        StudentResponse response = studentService.getProfile(currentStudent.getEmail());
        return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully", response));
    }
}
