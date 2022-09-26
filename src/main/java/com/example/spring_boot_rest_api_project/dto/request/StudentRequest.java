package com.example.spring_boot_rest_api_project.dto.request;

import com.example.spring_boot_rest_api_project.model.StudyFormat;
import com.example.spring_boot_rest_api_project.validation.PasswordValid;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class StudentRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @Column(unique = true)
    private String email;
    @Enumerated(value = EnumType.STRING)
    private StudyFormat studyFormat;
    @PasswordValid
    private String password;
    private Long companyId;
}
