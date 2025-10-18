package com.auth_service.auth_service.configuration;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:/Users/saurabhrathi/Documents/Projects/consult-me/configs/service.properties")
@ConfigurationProperties(prefix = "configs")
@Data
public class ServiceConfiguration {

    private String dbUrl;
}
