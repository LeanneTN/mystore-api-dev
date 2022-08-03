package com.example.mystoreapidev.service.impl;

import com.example.mystoreapidev.VO.ProductDetailVO;
import com.example.mystoreapidev.common.CONSTANT;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.common.ResponseCode;
import com.example.mystoreapidev.domain.Category;
import com.example.mystoreapidev.domain.Product;
import com.example.mystoreapidev.persistence.CategoryMapper;
import com.example.mystoreapidev.persistence.ProductMapper;
import com.example.mystoreapidev.service.ProductService;
import com.example.mystoreapidev.utils.ImageServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;

@Service("productService")
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Resource
    private ImageServerConfig imageServerConfig;

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

    private ProductDetailVO productToProductVO(Product product){
        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setId(product.getId());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setSubImages(productDetailVO.getSubImages());
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setName(product.getName());
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
}
