package com.example.mystoreapidev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mystoreapidev.common.CONSTANT;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.Category;
import com.example.mystoreapidev.persistence.CategoryMapper;
import com.example.mystoreapidev.service.CategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import javafx.print.Collation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service("categoryService")
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CommonResponse<Category> getCategory(Integer categoryId) {
        if(categoryId==null)
            return CommonResponse.createForError("id can't be null");
        if(CONSTANT.CATEGORY_ROOT.equals(categoryId))
            return CommonResponse.createForError("root node doesn't have detailed information");
        Category category = categoryMapper.selectById(categoryId);
        if(category==null)
            return CommonResponse.createForError("no category information in this ID");
        return CommonResponse.createForSuccess(category);
    }

    @Override
    public CommonResponse<List<Category>> getChildrenCategories(Integer categoryId) {
        if(categoryId==null)
            return CommonResponse.createForError("category id can't be null");
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", categoryId);
        List<Category> categoryList = categoryMapper.selectList(queryWrapper);
        //use apache's package collectionUtils to judge if a List is empty
        //this avoids the insecurity thread of apis before Java 8
        if(CollectionUtils.isEmpty(categoryList)){
            log.info("Didn't get children information");
        }

        return CommonResponse.createForSuccess(categoryList);
    }

    @Override
    public CommonResponse<List<Integer>> getCategoryAndAllChildren(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        List<Integer> categoryIdList = Lists.newArrayList();

        if(categoryId == null)
            return CommonResponse.createForSuccess(categoryIdList);
        findChildCategory(categoryId, categorySet);
        for(Category category : categorySet){
            categoryIdList.add(category.getId());
        }
        return CommonResponse.createForSuccess(categoryIdList);
    }

    //use the properties of set and call by reference of Java to achieve recursion method
    private Set<Category> findChildCategory(Integer categoryId, Set<Category> categorySet){
        //elements in Set can't repeat
        Category category = categoryMapper.selectById(categoryId);
        if(category!= null){
            categorySet.add(category);
        }
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", categoryId);
        List<Category> categoryList = categoryMapper.selectList(queryWrapper);
        for(Category categoryItem: categoryList){
            findChildCategory(categoryItem.getId(), categorySet);
        }

        return categorySet;
    }

    @Override
    public CommonResponse<Object> addCategory(String categoryName, Integer parentId) {
        return null;
    }

    @Override
    public CommonResponse<Object> updateCategory(Integer categoryId, String categoryName) {
        return null;
    }
}
