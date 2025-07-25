package com.rtb.image.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponse {

    private Long id;
    private String prompt;
    private BufferedImage image;
    private byte[] imageData;

    public ImageResponse(String prompt, BufferedImage image) {
        this.prompt = prompt;
        this.image = image;
    }

    public ImageResponse(String prompt, BufferedImage image, byte[] imageData) {
        this.prompt = prompt;
        this.image = image;
        this.imageData = imageData;
    }
}
