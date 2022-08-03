package com.example.mystoreapidev.service;

import com.example.mystoreapidev.VO.ProductDetailVO;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.Product;

public interface ProductService {
    public CommonResponse<ProductDetailVO> getProductDetail(Integer productId);
}
