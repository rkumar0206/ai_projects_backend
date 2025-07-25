package com.rtb.the_random_value.story.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoryRequestDTO {

    private String genre;
    private String targetAudience;
    private String centralConcept;
    private String keyElementsToInclude;
    private Integer length;
    
}
