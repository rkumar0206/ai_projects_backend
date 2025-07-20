package com.rtb.promptrefiner.controller;

import com.rtb.promptrefiner.PromptRefinerService;
import com.rtb.promptrefiner.model.RefinedPromptResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/prompt-refiner")
public class PromptRefinerController {

    private final PromptRefinerService promptRefinerService;

    @GetMapping("/refine")
    public ResponseEntity<Map<String, String>> refinePrompt(@RequestParam("rawText") String rawText) {

        Map<String, String> response = new HashMap<>();
        response.put("response", promptRefinerService.refineMyPrompt(rawText));

        return ResponseEntity.ok(response);
    }


}
