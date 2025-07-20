package com.rtb.the_random_value.colors.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaletteResponseDTO {
    private List<PaletteDTO> palette;
    private String themeName;
    private String rationale;
}
