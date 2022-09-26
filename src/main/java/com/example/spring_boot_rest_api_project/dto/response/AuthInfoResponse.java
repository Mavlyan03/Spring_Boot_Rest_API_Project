package com.example.spring_boot_rest_api_project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class AuthInfoResponse {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Set<String> roles;
    private LocalDateTime created;
    private Boolean isActive;

    public AuthInfoResponse(Long id, String username,
                            String email, String password,
                            Boolean isActive, LocalDateTime created) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.created = created;
    }

}
