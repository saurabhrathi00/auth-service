package com.auth_service.auth_service.services;

import com.auth_service.auth_service.configuration.SecretsConfiguration;
import com.auth_service.auth_service.enums.RoleEnum;
import com.auth_service.auth_service.exceptions.AuthFailedException;
import com.auth_service.auth_service.exceptions.ConflictException;
import com.auth_service.auth_service.exceptions.RoleNotFoundException;
import com.auth_service.auth_service.models.dao.RoleEntity;
import com.auth_service.auth_service.models.dao.UserEntity;
import com.auth_service.auth_service.models.request.ServiceTokenRequest;
import com.auth_service.auth_service.models.request.SigninRequest;
import com.auth_service.auth_service.models.response.AuthenticationResponse;
import com.auth_service.auth_service.models.response.RefreshTokenResponse;
import com.auth_service.auth_service.models.response.ServiceTokenResponse;
import com.auth_service.auth_service.repository.RoleRepository;
import com.auth_service.auth_service.repository.UserRepository;
import com.auth_service.auth_service.security.token.TokenPrincipal;
import com.auth_service.auth_service.security.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.time.Duration;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final SecretsConfiguration secretsConfiguration;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;


    @Transactional
    public AuthenticationResponse signIn(SigninRequest request) {
        String username = request.getUsername().trim();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthFailedException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthFailedException("Invalid username or password");
        }

        // Prepare JWT claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getId());

        // Include roles & scopes in claims
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            List<String> roles = user.getRoles().stream()
                    .map(RoleEntity::getName)
                    .toList();
            List<String> scopes = user.getRoles().stream()
                    .flatMap(r -> r.getScopes().stream())
                    .distinct()
                    .toList();

            claims.put("roles", roles);
            claims.put("scopes", scopes);
        }

        // Generate short-lived access token using configurable expiration
        Duration accessTokenExpiry = secretsConfiguration.getJwt().getAccessTokenExpiration();
        String accessToken = tokenProvider.issue(user.getUsername(), claims, accessTokenExpiry);

        // Generate refresh token using refresh token expiration
        Duration refreshTokenExpiry = secretsConfiguration.getJwt().getRefreshTokenExpiration();
        String refreshToken = tokenProvider.issue(user.getUsername(), claims, refreshTokenExpiry);

        log.info("User signed in: {}", username);

        return AuthenticationResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken) // you can add this field to AuthenticationResponse
                .tokenType(tokenProvider.tokenType())
                .expiresIn(accessTokenExpiry.toSeconds()) // dynamically set from config
                .message("Login successful")
                .build();
    }

    @Transactional
    public RefreshTokenResponse refreshToken(String refreshToken) {
        // 1. Validate the refresh token
        if (!tokenProvider.validate(refreshToken)) {
            throw new AuthFailedException("Invalid or expired refresh token");
        }

        // 2. Parse claims from the refresh token
        TokenPrincipal principal = tokenProvider.parse(refreshToken);
        String username = principal.getSubject();

        // 3. Load user from DB
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthFailedException("User not found"));

        // 4. Prepare new access token claims (same as signIn)
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getId());

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            List<String> roles = user.getRoles().stream()
                    .map(RoleEntity::getName)
                    .toList();
            List<String> scopes = user.getRoles().stream()
                    .flatMap(r -> r.getScopes().stream())
                    .distinct()
                    .toList();

            claims.put("roles", roles);
            claims.put("scopes", scopes);
        }

        // 5. Issue new short-lived access token
        String newAccessToken = tokenProvider.issue(
                username,
                claims,
                secretsConfiguration.getJwt().getAccessTokenExpiration()
        );

        return RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .expiresIn(secretsConfiguration.getJwt().getAccessTokenExpiration().getSeconds())
                .message("Access token refreshed successfully")
                .build();
    }

    public ServiceTokenResponse generateServiceToken(ServiceTokenRequest request) {

        // --- 1. Validate service credentials ---
        SecretsConfiguration.ServiceCredentials creds =
                secretsConfiguration.getServices().get(request.getClientId());

        if (creds == null || !creds.getPassword().equals(request.getClientSecret())) {
            throw new AuthFailedException("Invalid clientId or clientSecret");
        }

        Duration accessTokenExpiry = secretsConfiguration.getJwt().getServiceTokenExpiration();
        // Prepare JWT claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("audience", request.getAudience());
        claims.put("scopes", "internal_service_access");
        String accessToken = tokenProvider.issue(request.getClientId(), claims, accessTokenExpiry);

       return ServiceTokenResponse.builder()
                .token(accessToken)
                .expiresIn(accessTokenExpiry.toSeconds())
                .build();
    }

}
