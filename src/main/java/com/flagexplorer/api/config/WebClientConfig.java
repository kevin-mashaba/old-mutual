package com.flagexplorer.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    private final RestCountriesConfig config;

    public WebClientConfig(RestCountriesConfig config) {
        this.config = config;
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(config.getBaseUrl())
                .build();
    }
}
