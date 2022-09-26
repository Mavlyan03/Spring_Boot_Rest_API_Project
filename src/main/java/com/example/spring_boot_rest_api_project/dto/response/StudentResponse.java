package com.example.spring_boot_rest_api_project.dto.response;

import com.example.spring_boot_rest_api_project.model.StudyFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class StudentResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @Column(unique = true)
    private String email;
    @Enumerated(value = EnumType.STRING)
    private StudyFormat studyFormat;
    private String password;
    private String token;
    private Set<String> roles;
    private Long companyId;
    private LocalDateTime created;
    private Boolean isActive;
    public StudentResponse(Long id, String firstName, String lastName,
                           String phoneNumber, String email, StudyFormat studyFormat,
                           String password, Long companyId, Boolean isActive, LocalDateTime created) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.studyFormat = studyFormat;
        this.password = password;
        this.companyId = companyId;
        this.isActive = isActive;
        this.created = created;
    }
}
