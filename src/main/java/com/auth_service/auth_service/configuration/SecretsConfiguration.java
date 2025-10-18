package com.auth_service.auth_service.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.time.Duration;

@Data
@Configuration
@PropertySource("file:/Users/saurabhrathi/Documents/Projects/consult-me/secrets/secrets.properties")
@ConfigurationProperties(prefix = "secrets")
public class SecretsConfiguration {

    private Jwt jwt;
    private Datasource datasource;   // rename db → datasource to match secrets.datasource.*

    @Data
    public static class Jwt {
        private String secret;
        private String type;
        private Duration accessTokenExpiration;   // maps from access-token.expiration
        private Duration refreshTokenExpiration;  // maps from refresh-token.expiration

//        public Duration getAccessTokenExpiration() {
//            return Duration.parse("PT" + accessTokenExpiration.toUpperCase());
//        }
//
//        public Duration getRefreshTokenExpiration() {
//            return Duration.parse("PT" + refreshTokenExpiration.toUpperCase());
//        }
    }

    @Data
    public static class Datasource {
        private String username;
        private String password;
        private String driverClassName; // matches driver-class-name
    }
}


