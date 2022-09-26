package com.example.spring_boot_rest_api_project.dto.request;

import com.example.spring_boot_rest_api_project.validation.PasswordValid;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AuthInfoRequest {
    private String username;
    private String email;
    @PasswordValid
    private String password;
    private Set<String> roles;
}
