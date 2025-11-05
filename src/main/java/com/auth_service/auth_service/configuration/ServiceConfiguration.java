package com.auth_service.auth_service.configuration;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "configs")
@Data
public class ServiceConfiguration {

    private UserDb userDb;
    @Data
    public static class UserDb {
        private String name;
        private String url;
    }
}
