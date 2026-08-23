package com.lamzytech.news.service;

import com.lamzytech.news.dto.StudentResponse;
import com.lamzytech.news.entity.Student;
import com.lamzytech.news.exception.ResourceNotFoundException;
import com.lamzytech.news.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentResponse getProfile(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return StudentResponse.fromEntity(student);
    }
}
