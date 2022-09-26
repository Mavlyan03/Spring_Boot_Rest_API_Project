package com.example.spring_boot_rest_api_project.service;

import com.example.spring_boot_rest_api_project.dto.request.AuthInfoRequest;
import com.example.spring_boot_rest_api_project.dto.response.AuthInfoResponse;
import com.example.spring_boot_rest_api_project.dto.view.AuthInfoView;
import com.example.spring_boot_rest_api_project.model.AuthInfo;
import com.example.spring_boot_rest_api_project.model.Role;
import com.example.spring_boot_rest_api_project.repository.AuthInfoRepository;
import com.example.spring_boot_rest_api_project.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthInfoRepository authRepo;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthInfoResponse create(AuthInfoRequest request) {
        AuthInfo user = mapToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreated(LocalDateTime.now());
        user.setIsActive(user.getIsActive());
        AuthInfo authInfo = authRepo.save(user);
        return mapToResponse(authInfo);
    }

    private AuthInfo mapToEntity(AuthInfoRequest request) {
        AuthInfo authInfo = new AuthInfo(request.getUsername(), request.getEmail(), request.getPassword());
        Set<String> reqRoles = request.getRoles();
        Set<Role> roles = new HashSet<>();
        if (reqRoles == null) {
            Role userRole = roleRepository
                    .findByRoleName("USER")
                    .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
            roles.add(userRole);
        } else {
            reqRoles.forEach(r -> {
                switch (r) {
                    case "admin":
                        Role adminRole = roleRepository
                                .findByRoleName("ADMIN")
                                .orElseThrow(() -> new RuntimeException("Error, Role ADMIN is not found"));
                        roles.add(adminRole);
                        break;
                    case "manager":
                        Role modRole = roleRepository
                                .findByRoleName("MANAGER")
                                .orElseThrow(() -> new RuntimeException("Error, Role MODERATOR is not found"));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository
                                .findByRoleName("USER")
                                .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
                        roles.add(userRole);
                }
            });
        }
        authInfo.setRoles(roles);
        return authInfo;
    }

    private AuthInfoResponse mapToResponse(AuthInfo authInfo) {
        authInfo.setCreated(LocalDateTime.now());
        authInfo.setIsActive(authInfo.getIsActive());
        AuthInfoResponse authInfoResponse = new AuthInfoResponse(authInfo.getId(), authInfo.getUsername(),
                authInfo.getEmail(), authInfo.getPassword(), authInfo.getIsActive(), authInfo.getCreated());
        Set<Role> roles = authInfo.getRoles();
        Set<String> rolesSet = new HashSet<>();
        for (Role role : roles) {
            rolesSet.add(role.getRoleName());
        }
        authInfoResponse.setRoles(rolesSet);
        return authInfoResponse;
    }


    public AuthInfoView getAllUsersPagination(String text, int page, int size) {
        AuthInfoView view = new AuthInfoView();
        Pageable pageable = PageRequest.of(page - 1, size);
        view.setAuthInfoResponses(getUsers(search(text, pageable)));
        return view;
    }

    private List<AuthInfo> search(String name, Pageable pageable) {
        String text = name == null ? "" : name;
        return authRepo.searchByAuth(text.toUpperCase(), pageable);
    }

    public List<AuthInfoResponse> getUsers(List<AuthInfo> users) {
        List<AuthInfoResponse> responses = new ArrayList<>();
        Set<String> roles = new HashSet<>();
        for (AuthInfo user : users) {
            for (Role role : user.getRoles()) {
                roles.add(role.getRoleName());
            }
            user.setCreated(LocalDateTime.now());
            user.setIsActive(user.getIsActive());
            responses.add(new AuthInfoResponse(user.getId(), user.getUsername(), user.getEmail(),
                    user.getPassword(), roles, user.getCreated(), user.getIsActive()));
        }
        return responses;
    }
}
