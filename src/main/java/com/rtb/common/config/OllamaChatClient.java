package com.rtb.common.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaChatClient {

    @Bean
    public ChatClient getOllamaChatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder.build();
    }

}
