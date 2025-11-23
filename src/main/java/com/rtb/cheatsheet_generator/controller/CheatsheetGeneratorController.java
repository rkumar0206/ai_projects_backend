package com.rtb.cheatsheet_generator.controller;

import com.rtb.cheatsheet_generator.service.CheatsheetGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cheatsheet-generator")
public class CheatsheetGeneratorController {

    private final CheatsheetGeneratorService cheatsheetGeneratorService;

    @PostMapping
    public ResponseEntity<String> generateCheatSheet(@RequestParam String technology) {

        ResponseEntity<String> response;

        try {
            response = ResponseEntity.ok(cheatsheetGeneratorService.generateCheatSheet(technology));
        }catch (IllegalArgumentException e) {
            response = ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            response = ResponseEntity.internalServerError().body(e.getMessage());
        }

        return response;
    }

}
