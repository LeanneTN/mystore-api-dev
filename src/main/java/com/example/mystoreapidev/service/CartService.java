package com.example.mystoreapidev.service;

import com.example.mystoreapidev.VO.CartVO;
import com.example.mystoreapidev.common.CommonResponse;

public interface CartService {
    CommonResponse<CartVO> addCart(Integer userId, Integer productId, Integer quantity);

    CommonResponse<CartVO> updateCart(Integer userId, Integer productId, Integer quantity);

    CommonResponse<CartVO> deleteCartItems(Integer userId, String productIds);

    CommonResponse<CartVO> list(Integer userId);

    CommonResponse<Integer> getCartCount(Integer userId);

    CommonResponse<CartVO> updateAllCheckStatus(Integer userId, Integer checkStatus);

    CommonResponse<CartVO> updateCheckStatus(Integer userId, Integer productId, Integer checkStatus);
}
