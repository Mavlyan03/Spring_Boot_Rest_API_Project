package com.example.spring_boot_rest_api_project.dto.login;

import com.example.spring_boot_rest_api_project.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class Login {

    public LoginResponse toLoginView(String token, String message, Set<Role> roles) {
        LoginResponse loginResponse = new LoginResponse();
        Set<String> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.add(role.getRoleName());
        }
        loginResponse.setAuthorities(authorities);
        loginResponse.setMessage(message);
        loginResponse.setJwtToken(token);
        return loginResponse;
    }
}