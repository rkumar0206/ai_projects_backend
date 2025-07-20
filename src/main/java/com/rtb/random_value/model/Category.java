package com.rtb.random_value.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private Long categoryId;
    private String categoryName;
    private String categoryImageUrl;
}
