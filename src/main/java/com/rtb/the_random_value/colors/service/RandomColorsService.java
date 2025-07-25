package com.rtb.the_random_value.colors.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtb.common.service.CommonService;
import com.rtb.the_random_value.colors.dto.PaletteColorDTO;
import com.rtb.the_random_value.colors.dto.PaletteThemeDTO;
import com.rtb.the_random_value.colors.entity.PaletteColor;
import com.rtb.the_random_value.colors.entity.PaletteTheme;
import com.rtb.the_random_value.colors.mapper.PaletteThemeMapper;
import com.rtb.the_random_value.colors.repository.PaletteThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RandomColorsService {

    private final CommonService commonService;
    private final PaletteThemeRepository repository;
    private final PaletteThemeMapper mapper;

    public PaletteThemeDTO getRandomColorPalette() throws JsonProcessingException {

        String prompt = generatePrompt();
        String responseText = commonService.getPromptTextResult(prompt);
        System.out.println(responseText);

        if (StringUtils.hasLength(responseText)) {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseText.replace("```", "").replace("json", ""));
            JsonNode paletteNode = root.get("palette");

            List<PaletteColorDTO> paletteList = mapper.readValue(
                    paletteNode.toString(),
                    new TypeReference<>() {
                    }
            );

            JsonNode themeNameNode = root.get("themeName");
            String themeName = mapper.readValue(themeNameNode.toString(), new TypeReference<>() {
            });

            JsonNode rationaleNode = root.get("rationale");
            String rationale = mapper.readValue(rationaleNode.toString(), new TypeReference<>() {
            });

            PaletteThemeDTO paletteThemeDTO = new PaletteThemeDTO(paletteList, themeName, rationale);
            System.out.printf("palette response dto: " + paletteThemeDTO);

            return paletteThemeDTO;
        }

        return null;
    }

    public PaletteThemeDTO createPaletteTheme(PaletteThemeDTO dto) {
        PaletteTheme entity = mapper.toEntity(dto);
        entity.getPalette().forEach(color -> color.setPaletteTheme(entity));
        return mapper.toDTO(repository.save(entity));
    }

    public List<PaletteThemeDTO> getAllPaletteThemes() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public PaletteThemeDTO getPaletteThemeById(Long id) {
        PaletteTheme theme = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("PaletteTheme not found with id: " + id));
        return mapper.toDTO(theme);
    }

    public PaletteThemeDTO getPaletteByTheme(String themeName) {
        PaletteTheme theme = repository.findByThemeNameIgnoreCase(themeName)
                .orElseThrow(() -> new RuntimeException("PaletteTheme not found with theme: " + themeName));
        return mapper.toDTO(theme);
    }

    public PaletteThemeDTO updatePaletteTheme(Long id, PaletteThemeDTO updatedDTO) {
        PaletteTheme existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("PaletteTheme not found with id: " + id));

        existing.setThemeName(updatedDTO.getThemeName());
        existing.setRationale(updatedDTO.getRationale());

        // Update palette list
        existing.getPalette().clear();
        updatedDTO.getPalette().forEach(dto -> {
            PaletteColor color = new PaletteColor();
            color.setHexCode(dto.getHexCode());
            color.setColorName(dto.getColorName());
            color.setCategory(dto.getCategory());
            color.setPaletteTheme(existing);
            existing.getPalette().add(color);
        });

        return mapper.toDTO(repository.save(existing));
    }

    public void deletePaletteTheme(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("PaletteTheme not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private String generatePrompt() {

        return "You are an expert color palette designer. Your task is to generate a harmonious 5-color palette suitable for digital design, providing specific details for each color and for the palette as a whole.\n" +
                "\n" +
                "**Requirements:**\n" +
                "\n" +
                "1. **Palette Size:** Create a palette consisting of exactly 5 distinct colors.\n" +
                "\n" +
                "2. **Harmony:** Ensure the colors are visually cohesive and work well together.\n" +
                "\n" +
                "3. **Categorization:** For each of the 5 colors, assign *one* functional category from the following list:\n" +
                "\n" +
                "\t* `primary` (main brand or UI color)\n" +
                "\t* `accent` (secondary highlights or interactive elements)\n" +
                "\t* `warn/error` (for alerts, errors, or warnings)\n" +
                "\t* `neutral/background` (for text, backgrounds, or subtle elements)\n" +
                "\t* `secondary accent` (an additional accent color, distinct from the main accent)\n" +
                "4. **Theme:** Propose a concise, descriptive name for the overall theme or mood of the color palette (e.g., \"Lush Forest\", \"Urban Sunset\", \"Minimalist Modern\").\n" +
                "\n" +
                "5. **Rationale:** Provide a brief explanation of why these specific colors were chosen and how they align with the proposed theme.\n" +
                "\n" +
                "---\n" +
                "\n" +
                "**Example Output:**\n" +
                "\n" +
                "\n" +
                "{\n" +
                "  \"palette\": [\n" +
                "    {\n" +
                "      \"hexCode\": \"#004B49\",\n" +
                "      \"colorName\": \"Deep Teal\",\n" +
                "      \"category\": \"primary\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"hexCode\": \"#E2B842\",\n" +
                "      \"colorName\": \"Goldenrod\",\n" +
                "      \"category\": \"accent\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"hexCode\": \"#D9524B\",\n" +
                "      \"colorName\": \"Brick Red\",\n" +
                "      \"category\": \"warn/error\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"hexCode\": \"#F2F2F2\",\n" +
                "      \"colorName\": \"Cloud White\",\n" +
                "      \"category\": \"neutral/background\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"hexCode\": \"#7D9D9C\",\n" +
                "      \"colorName\": \"Dusty Sage\",\n" +
                "      \"category\": \"secondary accent\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"themeName\": \"Desert Oasis\",\n" +
                "  \"rationale\": \"The 'Desert Oasis' palette evokes the tranquility and warmth of a desert landscape at different times of day. 'Deep Teal' serves as the primary, representing the cool, life-giving water found in an oasis. 'Goldenrod' provides a vibrant accent, reminiscent of desert sunlight and golden sands. 'Brick Red' is chosen for warn/error, reflecting the strong, earthy tones of canyon walls. 'Cloud White' offers a clean, bright background, mirroring a clear desert sky. Finally, 'Dusty Sage' acts as a subtle secondary accent, inspired by the resilient desert flora, adding depth and organic feel to the overall harmonious theme.\"\n" +
                "}";
    }
}
