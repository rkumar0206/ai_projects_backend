package com.rtb.the_random_value.repositories.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtb.common.service.CommonService;
import com.rtb.the_random_value.repositories.dto.RandomRepositoryDTO;
import com.rtb.the_random_value.repositories.dto.RandomRepositoryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RandomRepoService {

    private final CommonService commonService;

    public RandomRepositoryResponseDTO getRandomRepositories(Integer count, String languagesUsed) throws JsonProcessingException {

        String prompt = generatePrompt(count, languagesUsed);

        String responseText = commonService.getPromptTextResult(prompt);
        System.out.println(responseText);

        if (StringUtils.hasLength(responseText)) {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseText.replace("```", "").replace("json", ""));

            JsonNode repositories = root.get("repositories");
            List<RandomRepositoryDTO> randomRepositoryDTOS = mapper.readValue(
                    repositories.toString(),
                    new TypeReference<>() {
                    }
            );
            return new RandomRepositoryResponseDTO(randomRepositoryDTOS);
        }

        return null;
    }

    private String generatePrompt(Integer count, String languagesUsed) {

        return "Your task is to provide the URL of a single, real, and publicly accessible repository.\n" +
                "The repository must be hosted on either GitHub (github.com).\n" +
                "Select an example of such a repository from your knowledge base.\n" +
                "Try to include some un-common repositories.\n" +
                "\n" +
                "Considerations:\n" +
                "Number of repository: " + (count == null ? "8" : count.toString()) + "\n" +
                "Languages: " + (StringUtils.hasLength(languagesUsed) ? languagesUsed : "Random") + "\n" +
                "\n" +
                "Output format:\n" +
                "\n" +
                "{\n" +
                "  \"repositories\": [\n" +
                "    {\n" +
                "      \"url\": (The url of the repository.),\n" +
                "      \"briefDescription\": (Brief description of the repository),\n" +
                "      \"languages\": [] (ALl technologies used in this repository)\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";
    }
}
