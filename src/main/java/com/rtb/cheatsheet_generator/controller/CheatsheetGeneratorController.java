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

//        byte[] pdfBytes = cheatsheetGeneratorService.generateCheatSheet(technology);
//
//        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + technology + "_cheatsheet.pdf")
//                .body(pdfBytes);

        return ResponseEntity.ok(cheatsheetGeneratorService.generateCheatSheet(technology));
    }

}
