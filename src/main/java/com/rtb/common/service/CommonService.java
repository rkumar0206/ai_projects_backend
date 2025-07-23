package com.rtb.common.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final Client client;
    public static final String MODEL = "gemini-2.5-flash";

    public String getPromptTextResult(String prompt) {

        GenerateContentResponse response =
                client.models.generateContent(
                        MODEL,
                        prompt,
                        GenerateContentConfig.builder()
                                .responseMimeType("text/plain")
                                //.responseMimeType("application/json")
                                .build()
                );

        return response.text();

    }
}
