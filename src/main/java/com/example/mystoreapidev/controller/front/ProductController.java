package com.example.mystoreapidev.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mystoreapidev.VO.ProductDetailVO;
import com.example.mystoreapidev.VO.ProductListVO;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.Product;
import com.example.mystoreapidev.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/product/")
@Validated
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("detail")
    public CommonResponse<ProductDetailVO> getProductDetail(
            @RequestParam @NotNull(message = "product id can't be null") Integer productId
    ){
        return productService.getProductDetail(productId);
    }

    @GetMapping("list")
    public CommonResponse<Page<ProductListVO>> getProductList(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "") String orderBy,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "2") int pageSize){
        return productService.getProductList(categoryId, keyword, orderBy, pageNum, pageSize);
    }
}
