package com.rtb.the_random_value.repositories.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RandomRepositoryDTO {
    private String url;
    private String briefDescription;
    private List<String> languages;
}
