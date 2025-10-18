package com.auth_service.auth_service.controllers;

import com.auth_service.auth_service.models.request.RefreshTokenRequest;
import com.auth_service.auth_service.models.request.SigninRequest;
import com.auth_service.auth_service.models.request.SignupRequest;
import com.auth_service.auth_service.models.response.AuthenticationResponse;
import com.auth_service.auth_service.models.response.RefreshTokenResponse;
import com.auth_service.auth_service.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(@Valid @RequestBody SignupRequest request) {
        AuthenticationResponse response = authService.signUp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signin(@Valid @RequestBody SigninRequest request) {
        AuthenticationResponse response = authService.signIn(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        RefreshTokenResponse response = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }
}
