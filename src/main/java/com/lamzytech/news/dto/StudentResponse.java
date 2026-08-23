package com.lamzytech.news.dto;

import com.lamzytech.news.entity.Role;
import com.lamzytech.news.entity.Student;
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
public class StudentResponse {

    private Long id;
    private String fullName;
    private String email;
    private String department;
    private String level;
    private Role role;
    private LocalDateTime createdAt;

    public static StudentResponse fromEntity(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .fullName(student.getFullName())
                .email(student.getEmail())
                .department(student.getDepartment())
                .level(student.getLevel())
                .role(student.getRole())
                .createdAt(student.getCreatedAt())
                .build();
    }
}
