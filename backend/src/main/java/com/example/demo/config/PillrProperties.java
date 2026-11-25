package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pillr")
public record PillrProperties(
        String apiKey,
        String annoncesUrl,
        int maxResults
) {
}
