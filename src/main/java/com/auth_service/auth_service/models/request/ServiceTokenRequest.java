package com.auth_service.auth_service.models.request;


import lombok.Data;

import java.util.List;

@Data
public class ServiceTokenRequest {
    private String clientId;
    private String clientSecret;
    private String audience;
    private List<String> scopes = null;
}
