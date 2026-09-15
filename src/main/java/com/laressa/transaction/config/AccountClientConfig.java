package com.laressa.transaction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AccountClientConfig {

    @Bean
    public RestClient accountRestClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }
}