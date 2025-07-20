package com.rtb.promptrefiner;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromptRefinerService {

    private final Client client;
    public static final String MODEL = "gemini-2.5-flash";

    public String refineMyPrompt(String rawText) {

        String prompt = generatePrompt(rawText);

        GenerateContentResponse response =
                client.models.generateContent(
                        MODEL,
                        prompt,
                        GenerateContentConfig.builder()
                                .responseMimeType("text/plain")
                                //.responseMimeType("application/json")
                                .build()
                );

        System.out.println(response.text());
        return response.text();
    }

    private String generatePrompt(String rawText) {

        return "You are PromptRefiner, an expert in prompt engineering for large language models (LLMs) like GPT and Gemini.\n" +
                "Your job is to improve raw, vague, or unclear prompts. Given a user’s input, you will:\n" +
                "1. Rewrite the prompt in a clear, structured format.\n" +
                "2. Make the intent explicit.\n" +
                "3. Suggest improvements or additional context if needed.\n" +
                "4. Optionally include an input/output example.\n" +
                "5. Use best practices from prompt engineering.\n\n" +
                "6. Only provide the refined prompt for the rawText and nothing else.\n" +
                "rawText: " + rawText + "\n" +
                "Your output must follow this format:\n" +
                "\n" +
                "Refined Prompt:\n\n" +
                "<your improved version>\n";
    }
}
