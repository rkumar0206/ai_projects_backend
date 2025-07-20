package com.rtb.promptrefiner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefinedPromptResponse {
    private String refinedPrompt;
    private List<String> whyThisIsBetter;
    private List<String> tips;
    // Getters/Setters
}
