package com.rtb.random_value.service;

import com.rtb.random_value.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.rtb.random_value.constant.Constants.*;

@Service
@RequiredArgsConstructor
public class CategoryService {
    public List<Category> getCategoryList() {

        List<Category> categories = new ArrayList<>();

        categories.add(new Category(1L, NUMBERS_AND_ALPHABETS, "/images/numbers_and_alphabets_img.svg"));
        categories.add(new Category(2L, DATES, "/images/dates_img.svg"));
        categories.add(new Category(3L, FOOD_RECIPES, "/images/food_recipe_img.svg"));
        categories.add(new Category(4L, IMAGES, "/images/images_img.svg"));
        categories.add(new Category(5L, SHOPPING, "/images/shopping.svg"));
        categories.add(new Category(6L, GITHUB_REPOSITORIES, "/images/github_img.svg"));
        categories.add(new Category(7L, COLORS, "/images/colors_img.svg"));
        categories.add(new Category(8L, VIDEOS, "/images/video_img.svg"));
        categories.add(new Category(9L, ARTICLES, "/images/article_img.svg"));

        return categories;
    }
}
