package com.example.mystoreapidev.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mystoreapidev.VO.ProductDetailVO;
import com.example.mystoreapidev.VO.ProductListVO;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.Product;

public interface ProductService {
    public CommonResponse<ProductDetailVO> getProductDetail(Integer productId);

    public CommonResponse<Page<ProductListVO>> getProductList(Integer categoryId, String keyword, String orderBy, int pageNum, int pageSize);
}
