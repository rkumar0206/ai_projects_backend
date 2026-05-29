package com.rtb.common.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final Client client;
    private final ChatClient chatClient;

    @Value("${ai.api.model}")
    private String model;

    public String getPromptTextResult(String prompt) {

        GenerateContentResponse response =
                client.models.generateContent(
                        model,
                        prompt,
                        GenerateContentConfig.builder()
                                .responseMimeType("text/plain")
                                //.responseMimeType("application/json")
                                .build()
                );

        return response.text();

    }

    public String getPromptTextResultFromOllama(String prompt) {
        return chatClient.prompt().user(prompt).call().content();
    }
}
