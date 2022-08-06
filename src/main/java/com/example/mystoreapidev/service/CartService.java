package com.example.mystoreapidev.service;

import com.example.mystoreapidev.VO.CartVO;
import com.example.mystoreapidev.common.CommonResponse;

public interface CartService {
    public CommonResponse<CartVO> addCart(Integer userId, Integer productId, Integer quantity);
}
