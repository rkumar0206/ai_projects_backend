package com.rtb;

import com.google.genai.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class RtbAIApplication implements CommandLineRunner {

    @Value("${ai.api.key}")
    private String apiKey;

    public static void main(String[] args) {
        SpringApplication.run(RtbAIApplication.class, args);
    }

    @Bean
    public Client getGoogleAIClient() {
        return Client.builder().apiKey(apiKey).build();
    }

    @Override
    public void run(String... args) throws Exception {

        //deploymentConfigAiGenerationService.test();
    }
}
