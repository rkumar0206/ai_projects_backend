package com.rtb.the_random_value.images.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponse {

    private String prompt;
    private BufferedImage image;
}
