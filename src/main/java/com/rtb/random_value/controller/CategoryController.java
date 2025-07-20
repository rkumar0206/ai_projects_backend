package com.rtb.random_value.controller;

import com.rtb.random_value.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

import static com.rtb.random_value.constant.Constants.DATES;
import static com.rtb.random_value.constant.Constants.NUMBERS_AND_ALPHABETS;


@Controller
@RequestMapping("/the-random-value/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping()
    public ModelAndView categoriesHome() {

        return new ModelAndView("categories", "categories", categoryService.getCategoryList());
    }

    @RequestMapping("/" + NUMBERS_AND_ALPHABETS)
    public ModelAndView numberAndAlphabetsCategory() {

        return new ModelAndView("numbers_and_alphabets");
    }

    @RequestMapping("/" + DATES)
    public ModelAndView dateCategory() {

        return new ModelAndView("dates");
    }

    @RequestMapping("/" + NUMBERS_AND_ALPHABETS + "/uuid")
    @ResponseBody
    public String getUUID() {

        return UUID.randomUUID().toString();
    }
}
