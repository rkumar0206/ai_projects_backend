package com.rtb.the_random_value.colors.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rtb.the_random_value.colors.entity.PaletteTheme;
import com.rtb.the_random_value.colors.service.RandomColorsService;
import com.rtb.the_random_value.colors.dto.PaletteThemeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/the-random-value/colors/palette")
public class RandomColorsController {

    private final RandomColorsService randomColorsService;

    @GetMapping
    public ResponseEntity<PaletteThemeDTO> getRandomPalette() throws JsonProcessingException {

        return ResponseEntity.ok(randomColorsService.getRandomColorPalette());
    }

    @PostMapping("/db/save")
    public ResponseEntity<PaletteThemeDTO> create(@RequestBody PaletteThemeDTO paletteTheme) {
        return ResponseEntity.ok(randomColorsService.createPaletteTheme(paletteTheme));
    }

    @GetMapping("/db/get-all")
    public ResponseEntity<List<PaletteThemeDTO>> getAll() {
        return ResponseEntity.ok(randomColorsService.getAllPaletteThemes());
    }

    @GetMapping("/db/by-id/{id}")
    public ResponseEntity<PaletteThemeDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(randomColorsService.getPaletteThemeById(id));
    }

    @GetMapping("/db/get-by-theme/{theme}")
    public ResponseEntity<PaletteThemeDTO> getById(@PathVariable String theme) {
        return ResponseEntity.ok(randomColorsService.getPaletteByTheme(theme));
    }

    @PutMapping("/db/update/{id}")
    public ResponseEntity<PaletteThemeDTO> update(@PathVariable Long id, @RequestBody PaletteThemeDTO updatedTheme) {
        return ResponseEntity.ok(randomColorsService.updatePaletteTheme(id, updatedTheme));
    }

    @DeleteMapping("/db/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        randomColorsService.deletePaletteTheme(id);
        return ResponseEntity.noContent().build();
    }

}
