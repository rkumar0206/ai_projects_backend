package com.rtb.the_random_value.images.controller;

import com.rtb.the_random_value.images.dto.ImageResponse;
import com.rtb.the_random_value.images.service.ImageGenerationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/the-random-value/image")
public class RandomImageGenerationController {

    private final ImageGenerationService imageGenerationService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> getImageAndText(@RequestParam(value = "keywords", required = false) String keywords) {
        try {

            ImageResponse imageResponse = imageGenerationService.generateImage(keywords);

            BufferedImage image = imageResponse.getImage();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());

            Map<String, String> response = new HashMap<>();
            response.put("description", imageResponse.getPrompt());
            response.put("image", "data:image/png;base64," + base64Image);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate or send image", e);
        }
    }

}
