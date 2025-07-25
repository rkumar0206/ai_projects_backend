package com.rtb.the_random_value.colors.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaletteThemeDTO {
    private List<PaletteColorDTO> palette;
    private String themeName;
    private String rationale;
}
