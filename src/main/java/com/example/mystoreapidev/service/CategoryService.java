package com.example.mystoreapidev.service;

import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.Category;

import java.util.List;

public interface CategoryService {
    //get the detail of single category
    CommonResponse<Category> getCategory(Integer categoryId);

    //get the first layer child categories
    CommonResponse<List<Category>> getChildrenCategories(Integer categoryId);

    //if return much information once, we can separate it into few parts, return them one by one
    //get all child categories' id
    CommonResponse<List<Integer>> getCategoryAndAllChildren(Integer category);

    //add category
    CommonResponse<Object> addCategory(String categoryName, Integer parentId);

    //update name
    CommonResponse<Object> updateCategory(Integer categoryId, String categoryName);
}
