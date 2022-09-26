package com.example.spring_boot_rest_api_project.dto.request;

import com.example.spring_boot_rest_api_project.validation.PasswordValid;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class InstructorRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @Column(unique = true)
    private String email;
    private String specialization;
    @PasswordValid
    private String password;
    private Long companyId;
}
