package com.example.spring_boot_rest_api_project.api;

import com.example.spring_boot_rest_api_project.dto.login.Login;
import com.example.spring_boot_rest_api_project.dto.request.AuthInfoRequest;
import com.example.spring_boot_rest_api_project.dto.login.LoginRequest;
import com.example.spring_boot_rest_api_project.dto.response.AuthInfoResponse;
import com.example.spring_boot_rest_api_project.dto.login.LoginResponse;
import com.example.spring_boot_rest_api_project.dto.view.AuthInfoView;
import com.example.spring_boot_rest_api_project.exception.NotFoundException;
import com.example.spring_boot_rest_api_project.model.AuthInfo;
import com.example.spring_boot_rest_api_project.repository.AuthInfoRepository;
import com.example.spring_boot_rest_api_project.security.jwt.JwtTokenUtil;
import com.example.spring_boot_rest_api_project.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/jwt_token")
public class AuthController {

    private final AuthService authService;
    private final AuthInfoRepository authInfoRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final Login login;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> getLogin(@RequestBody @Valid LoginRequest request) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
            authenticationManager.authenticate(token);
            AuthInfo authInfo = authInfoRepository.findByEmail(token.getName()).orElseThrow(
                    () -> new NotFoundException("User with email %s not found"));
            return ResponseEntity.ok(login.toLoginView(jwtTokenUtil.generateToken(authInfo), "Successfully", authInfo.getRoles()));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(login.toLoginView("", "Login_failed", null));
        }
    }

    @PostMapping("/registration")
    public AuthInfoResponse registration(@RequestBody @Valid  AuthInfoRequest request) {
        return authService.create(request);
    }

    @GetMapping("/searchByUser")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(description = "ADMIN can searching the companies")
    public AuthInfoView getAllUsersPagination(@RequestParam(value = "text", required = false) String text,
                                              @RequestParam int page,
                                              @RequestParam int size) {
        return authService.getAllUsersPagination(text, page, size);
    }
}
