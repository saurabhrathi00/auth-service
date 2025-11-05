package com.auth_service.auth_service.controllers;

import com.auth_service.auth_service.models.request.ServiceTokenRequest;
import com.auth_service.auth_service.models.response.ServiceTokenResponse;
import com.auth_service.auth_service.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalAuthController {

    private final AuthenticationService authenticationService; // your service to create JWTs

    @PostMapping("/token")
    public ResponseEntity<ServiceTokenResponse> getServiceToken(@RequestBody ServiceTokenRequest request) {
        return ResponseEntity.ok(authenticationService.generateServiceToken(request));
    }
}

