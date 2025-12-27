package com.auth_service.auth_service.configuration;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "configs")
@Data
public class ServiceConfiguration {

    private UserDb userDb;
    private Auth auth;

    @Data
    public static class UserDb {
        private String name;
        private String url;
    }

    @Data
    public static class Auth {
        private boolean enabled;
        private Policy policy;
    }

    @Data
    public static class Policy {
        /**
         * Map<calleeService, PolicyRule>
         */
        private Map<String, PolicyRule> services;
    }

    @Data
    public static class PolicyRule {
        private List<String> scopes;
    }
}
