package com.rtb.code_equivalent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeEquivalentDTO {

    private String source;
    private String target;
    private String tool;

}
