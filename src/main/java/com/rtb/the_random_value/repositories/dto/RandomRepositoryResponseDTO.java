package com.rtb.the_random_value.repositories.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RandomRepositoryResponseDTO {
    private List<RandomRepositoryDTO> repositories;
}
