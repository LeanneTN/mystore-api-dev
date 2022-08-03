package com.example.mystoreapidev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mystoreapidev.VO.ProductDetailVO;
import com.example.mystoreapidev.VO.ProductListVO;
import com.example.mystoreapidev.common.CONSTANT;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.common.ResponseCode;
import com.example.mystoreapidev.domain.Category;
import com.example.mystoreapidev.domain.Product;
import com.example.mystoreapidev.persistence.CategoryMapper;
import com.example.mystoreapidev.persistence.ProductMapper;
import com.example.mystoreapidev.service.ProductService;
import com.example.mystoreapidev.utils.ImageServerConfig;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service("productService")
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Resource
    private ImageServerConfig imageServerConfig;

    @Autowired
    private CategoryServiceImpl categoryService;

    @Override
    public CommonResponse<ProductDetailVO> getProductDetail(Integer productId){
        if(productId==null){
            return CommonResponse.createForError(ResponseCode.ARGUMENT_ILLEGAL.getCode(), ResponseCode.ARGUMENT_ILLEGAL.getDescription());
        }

        Product product = productMapper.selectById(productId);
        if(product==null){
            return CommonResponse.createForError("product is not exists");
        }
        //when front end get the detail of a product, need consider the status
        if(product.getStatus()!= CONSTANT.ProductStatus.ON_SALE.getCode()){
            return CommonResponse.createForError("product is not on sale");
        }

        ProductDetailVO productDetailVO = productToProductVO(product);

        return CommonResponse.createForSuccess(productDetailVO);
    }

    @Override
    public CommonResponse<Page<ProductListVO>> getProductList(Integer categoryId, String keyword, String orderBy, int pageNum, int pageSize){
        if(StringUtils.isBlank(keyword) && categoryId==null){
            return CommonResponse.createForError("category id and keyword can't both be null");
        }
        if(categoryId!=null){
            Category category = categoryMapper.selectById(categoryId);
            if(category==null && StringUtils.isBlank(keyword)){
                log.info("no information for category id as {}, and keyword is null", categoryId);
                return CommonResponse.createForSuccess();
            }
        }

        Page<Product> result = new Page<>();
        result.setCurrent(pageNum);
        result.setSize(pageSize);

        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();

        List<Integer> categoryIdList = categoryService.getCategoryAndAllChildren(categoryId).getData();
        if(categoryIdList.size()!=0){
            queryWrapper.in("category_id", categoryIdList);
        }
        if(StringUtils.isBlank(keyword)){
            queryWrapper.like("name", "%"+keyword+"%");
        }

        //sort according to price
        //price_asc price_desc
        if(StringUtils.isNotBlank(orderBy)){
            if(StringUtils.equals(orderBy, CONSTANT.PRODUCT_BY_ASC)){
                queryWrapper.orderByAsc("price");
            }else if(StringUtils.equals(orderBy, CONSTANT.PRODUCT_BY_DESC)){
                queryWrapper.orderByDesc("price");
            }
        }

        result = productMapper.selectPage(result, queryWrapper);
        Page<ProductListVO> productListVOPage = toProductListPageVO(result);

        return CommonResponse.createForSuccess(productListVOPage);
    }

    private ProductDetailVO productToProductVO(Product product){
        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setId(product.getId());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setName(product.getName());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());

        //time stamp format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        productDetailVO.setCreateTime(formatter.format(product.getCreateTime()));
        productDetailVO.setUpdateTime(formatter.format(product.getUpdateTime()));

        Category category = categoryMapper.selectById(product.getCategoryId());
        productDetailVO.setParentCategoryId(category.getParentId());

        productDetailVO.setImageServer(imageServerConfig.getUrl()+","+imageServerConfig.getUsername()+","+imageServerConfig.getPassword());

        return productDetailVO;
    }

    private ProductListVO productToProductListVO(Product product){
        ProductListVO productListVO = new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setSubtitle(product.getSubtitle());
        productListVO.setPrice(product.getPrice());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setName(product.getName());
        productListVO.setStatus(product.getStatus());

        productListVO.setImageServer(imageServerConfig.getUrl()+","+imageServerConfig.getUsername()+","+imageServerConfig.getPassword());
        return productListVO;
    }

    //Page<Product> -> Page<ProductListVO>
    private Page<ProductListVO> toProductListPageVO(Page<Product> result){
        List<ProductListVO> productListVOS = Lists.newArrayList();
        for(Product item : result.getRecords()){
            ProductListVO productListVO = productToProductListVO(item);
            productListVOS.add(productListVO);
        }

        Page<ProductListVO> newResult = new Page<>();
        newResult.setCurrent(result.getCurrent());
        newResult.setTotal(result.getTotal());
        newResult.setSize(result.getSize());

        newResult.setRecords(productListVOS);
        return newResult;
    }
}
