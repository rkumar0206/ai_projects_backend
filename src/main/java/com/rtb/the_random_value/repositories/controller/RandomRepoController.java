package com.rtb.the_random_value.repositories.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rtb.the_random_value.repositories.dto.RandomRepositoryResponseDTO;
import com.rtb.the_random_value.repositories.service.RandomRepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/the-random-value/repositories")
@RequiredArgsConstructor
public class RandomRepoController {

    private final RandomRepoService randomRepoService;

    @GetMapping()
    public ResponseEntity<RandomRepositoryResponseDTO> getRandomRepositories(
            @RequestParam(value = "count", required = false) Integer count,
            @RequestParam(value = "languagesUsed", required = false) String languagesUsed
    ) throws JsonProcessingException {

        return ResponseEntity.ok(randomRepoService.getRandomRepositories(count, languagesUsed));
    }
}
