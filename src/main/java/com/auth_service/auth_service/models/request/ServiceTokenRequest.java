package com.auth_service.auth_service.models.request;


import lombok.Data;

@Data
public class ServiceTokenRequest {
    private String clientId;
    private String clientSecret;
    private String audience; // the target service
}
