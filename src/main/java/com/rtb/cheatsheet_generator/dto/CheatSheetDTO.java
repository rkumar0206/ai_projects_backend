package com.rtb.cheatsheet_generator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheatSheetDTO {

    private Long id;
    private String technology;
    private String cheatSheetHtml;
}
