package com.rtb.the_random_value.colors.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rtb.the_random_value.colors.service.RandomColorsService;
import com.rtb.the_random_value.colors.dto.PaletteResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/the-random-value/colors")
public class RandomColorsController {

    private final RandomColorsService randomColorsService;

    @GetMapping("/palette")
    public ResponseEntity<PaletteResponseDTO> getRandomPalette() throws JsonProcessingException {

        return ResponseEntity.ok(randomColorsService.getRandomColorPalette());
    }

}
