package com.example.mystoreapidev.controller.admin;

import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.Category;
import com.example.mystoreapidev.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category/")
@Validated
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("get_children_categories")
    public CommonResponse<List<Category>> getChildrenCategories(
            @RequestParam(defaultValue = "0") Integer categoryId
    ){
        return categoryService.getChildrenCategories(categoryId);
    }

    @GetMapping("get_all_children_categories")
    public CommonResponse<List<Integer>> getAllChildrenCategories(
            @RequestParam(defaultValue = "0") Integer categoryId
    ){
        return categoryService.getCategoryAndAllChildren(categoryId);
    }

}
