package com.rtb.common.config;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiClientConfig {

    @Value("${ai.api.key}")
    private String apiKey;

    @Bean
    public Client getGoogleAIClient() {
        return Client.builder().apiKey(apiKey).build();
    }

}
