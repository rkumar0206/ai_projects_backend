package com.rtb.promptrefiner.service;

import com.rtb.common.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromptRefinerService {

    private final CommonService commonService;

    public String refineMyPrompt(String rawText) {

        String prompt = generatePrompt(rawText);
        return commonService.getPromptTextResult(prompt);
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
