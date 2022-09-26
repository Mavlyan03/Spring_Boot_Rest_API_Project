package com.example.spring_boot_rest_api_project.dto.response;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class InstructorResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @Column(unique = true)
    private String email;
    private String specialization;
    private String password;
    private String token;
    private Set<String> role;
    private Long companyId;
    private LocalDateTime created;
    private Boolean isActive;

    public InstructorResponse(Long id, String firstName, String lastName, String phoneNumber,
                              String email, String specialization, String password,
                              Long companyId, Boolean isActive, LocalDateTime created) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.specialization = specialization;
        this.password = password;
        this.companyId = companyId;
        this.isActive = isActive;
        this.created = created;
    }
}