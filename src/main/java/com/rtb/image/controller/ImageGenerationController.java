package com.rtb.image.controller;

import com.rtb.image.dto.ImageResponse;
import com.rtb.image.dto.PartialImageResponse;
import com.rtb.image.service.ImageGenerationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/generator/image")
public class ImageGenerationController {

    private final ImageGenerationService imageGenerationService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> getImageAndText(@RequestParam(value = "prompt", required = false) String prompt) {
        try {

            ImageResponse imageResponse = imageGenerationService.generateImageByPrompt(prompt);

            String base64Image;

            if (imageResponse.getImageData() != null) {
                base64Image = Base64.getEncoder().encodeToString(imageResponse.getImageData());
            }else {

                BufferedImage image = imageResponse.getImage();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "png", baos);
                base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());
            }

            Map<String, String> response = new HashMap<>();
            response.put("description", imageResponse.getPrompt());
            response.put("image", "data:image/png;base64," + base64Image);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate or send image", e);
        }
    }

    @GetMapping("/all-prompts")
    public ResponseEntity<List<PartialImageResponse>> getAllSavedPrompt() {
        return ResponseEntity.ok(imageGenerationService.getAllSavedImagePrompt());
    }

    @GetMapping(path = "/saved-image-by-prompt", produces = MediaType.IMAGE_PNG_VALUE)
    public void getSavedImageByPrompt(HttpServletResponse response, @RequestParam("prompt") String prompt) throws IOException {
        try {
            BufferedImage image = imageGenerationService.getImageByPrompt(prompt).getImage(); // Returns BufferedImage
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            ImageIO.write(image, "png", response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate or send image", e);
        }
    }

    @GetMapping(path = "/buffered-image", produces = MediaType.IMAGE_PNG_VALUE)
    public void getImage(HttpServletResponse response, @RequestParam("prompt") String prompt) throws IOException {
        try {
            BufferedImage image = imageGenerationService.generateImageByPrompt(prompt).getImage(); // Returns BufferedImage
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            ImageIO.write(image, "png", response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate or send image", e);
        }
    }

    @DeleteMapping("/saved-image/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        imageGenerationService.deleteImageById(id);
        return ResponseEntity.noContent().build();
    }
}
