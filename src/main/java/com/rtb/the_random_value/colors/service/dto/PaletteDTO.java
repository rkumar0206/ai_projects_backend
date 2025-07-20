package com.rtb.the_random_value.colors.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaletteDTO {
    private String hexCode;
    private String colorName;
    private String category;
}
