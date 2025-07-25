package com.rtb.image.service;

import com.google.genai.Client;
import com.google.genai.types.*;
import com.rtb.common.service.CommonService;
import com.rtb.image.dto.ImageResponse;
import com.rtb.image.dto.PartialImageResponse;
import com.rtb.image.entity.Image;
import com.rtb.image.repository.ImageRepository;
import com.rtb.image.util.ImageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageGenerationService {

    private final Client client;
    private final CommonService commonService;
    private final ImageRepository imageRepository;

    public ImageResponse generateImageByPrompt(String prompt) throws IOException {

        if (!StringUtils.hasLength(prompt)) {
            return null;
        }

        Optional<Image> imageFromDB = imageRepository.findByPromptIgnoreCase(prompt);
        if (imageFromDB.isPresent()) {
            Image image = imageFromDB.get();
            return new ImageResponse(image.getPrompt(), ImageUtil.byteArrayToBufferedImage(image.getImageData()), image.getImageData());
        }

        ImageResponse imageResponse = generateImage(prompt.trim());
        saveImage(imageResponse, "png");
        return imageResponse;
    }

    public ImageResponse generateRandomImage(String keywords) throws IOException {

        String imagePrompt = generateImagePrompt(keywords);

        ImageResponse imageResponse = generateImage(imagePrompt);
        saveImage(imageResponse, "png");
        return imageResponse;
    }

    public Page<ImageResponse> getAllSavedImage(Pageable pageable) throws IOException {

        Page<Image> images = imageRepository.findAll(pageable);

        List<ImageResponse> imageResponses = new ArrayList<>();

        for (Image image : images) {

            imageResponses.add(new ImageResponse(image.getPrompt(), ImageUtil.byteArrayToBufferedImage(image.getImageData())));
        }

        return new PageImpl<>(imageResponses, pageable, images.getTotalElements());
    }

    public List<PartialImageResponse> getAllSavedImagePrompt() {

        return imageRepository.findAllPromptsExceptFromTheRecipe();
    }

    public ImageResponse getImageByPrompt(String prompt) throws IOException {

        Image image = imageRepository.findByPromptIgnoreCase(prompt).orElseThrow(() -> new RuntimeException("No Image found with this prompt"));
        return new ImageResponse(image.getPrompt(), ImageUtil.byteArrayToBufferedImage(image.getImageData()));
    }


    public void saveImage(ImageResponse imageResponse, String format) throws IOException {

        if (imageResponse != null) {

            byte[] imageData = ImageUtil.bufferedImageToByteArray(imageResponse.getImage(), format);
            saveImage(imageResponse.getPrompt(), imageData);
        }
    }

    public void saveImage(String prompt, byte[] image) throws IOException {
        imageRepository.save(new Image(null, prompt, image));
    }

    public void deleteImageById(Long id) {
        imageRepository.deleteById(id);
    }

    private ImageResponse generateImage(String prompt) throws IOException {

        String modelName = "gemini-2.0-flash-preview-image-generation";

        GenerateContentConfig config = GenerateContentConfig.builder()
                .responseModalities(List.of("TEXT", "IMAGE"))
                .build();

        Content content = Content.fromParts(Part.fromText(prompt));

        GenerateContentResponse response = client.models.generateContent(
                modelName,
                content,
                config
        );

        Optional<Candidate> candidateOpt = response.candidates().flatMap(candidates -> candidates.stream().findFirst());

        if (candidateOpt.isEmpty()) {
            System.out.println("No candidate found.");
            return null;
        }

        Candidate candidate = candidateOpt.get();
        Optional<Content> contentOpt = candidate.content();

        if (contentOpt.isEmpty()) {
            System.out.println("No content found.");
            return null;
        }

        List<Part> parts = contentOpt.get().parts().orElse(List.of());

        for (Part part : parts) {
            if (part.inlineData().isPresent()) {
                Blob blob = part.inlineData().get();
                return new ImageResponse(prompt, ImageIO.read(new ByteArrayInputStream(blob.data().get())));
            }
        }

//        for (Part part : parts) {
//
//            // 1. Handle Text (description)
//            part.text().ifPresent(text -> {
//                System.out.println("📝 Description: " + text);
//            });
//
//            // 2. Handle Image (inlineData)
//            part.inlineData().ifPresent(blob -> {
//                //String mimeType = blob.mimeType().orElse("image/png");
//
//                // Convert base64-encoded image to bytes
//                blob.data().ifPresent(dataBytes -> {
//                    try {
//                        BufferedImage image = ImageIO.read(new ByteArrayInputStream(dataBytes));
//                        if (image != null) {
//                            File outputFile = new File("gemini-output.png");
//                            ImageIO.write(image, "png", outputFile);
//                            System.out.println("✅ Image saved: gemini-output.png");
//                        } else {
//                            System.out.println("⚠️ Could not decode image");
//                        }
//                    } catch (IOException e) {
//                        System.err.println("❌ Failed to save image: " + e.getMessage());
//                    }
//                });
//            });
//        }
        return null;

    }

    private String generateImagePrompt(String keywords) {

        String prompt = "As an expert AI image prompt generator, your task is to create a single, highly descriptive, and imaginative text prompt suitable for advanced AI art generation models (e.g., Midjourney, DALL-E, Stable Diffusion).\n" +
                "\n" +
                "**Key Requirements:**\n" +
                "1.  **Creativity & Specificity:** Generate a unique and vivid scene description. Include details such as the main subject(s), action, setting, time of day/lighting, mood/atmosphere, and an identifiable art style or aesthetic (e.g., 'cyberpunk', 'renaissance painting', 'photorealistic', 'fantasy art', 'abstract').\n" +
                "2.  **Keyword Integration (if provided):**\n" +
                "    *   If the user provides specific keywords, integrate them prominently and naturally into the core concept of the generated image prompt. These keywords should act as central elements or critical modifiers (e.g., a primary subject, a key object, or a dominant mood).\n" +
                "    *   If no keywords are provided, generate a completely original and varied prompt, exploring diverse subjects, styles, and scenarios without external constraints.\n" +
                "3.  **Format:** The output must be a single, continuous text string (e.g., a comma-separated list of descriptors or a coherent sentence/phrase), ready to be directly input into an image generator.\n" +
                "Keywords: " + keywords;

        return commonService.getPromptTextResult(prompt);
    }

}
