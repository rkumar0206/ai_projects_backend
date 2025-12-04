package com.rtb.cheatsheet_generator.controller;

import com.rtb.cheatsheet_generator.service.CheatsheetGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

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

    @GetMapping("/technologies")
    public ResponseEntity<List<String>> findAllTechnologies() {
        return ResponseEntity.ok(cheatsheetGeneratorService.findAllTechnologies());
    }

    @DeleteMapping("/technologies")
    public ResponseEntity<HashMap<String, String>> deleteTechnology(@RequestParam String technology) {
        cheatsheetGeneratorService.deleteByTechnology(technology);

        HashMap<String, String> response = new HashMap<>();
        response.put("message", "technology deleted");

        return ResponseEntity.ok(response);
    }

}
