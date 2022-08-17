package com.example.mystoreapidev.service;

import com.example.mystoreapidev.VO.OrderCartItemVO;
import com.example.mystoreapidev.VO.OrderVO;
import com.example.mystoreapidev.common.CommonResponse;

public interface OrderService {
    CommonResponse<OrderVO> create(Integer userId, Integer addressId);

    CommonResponse<OrderCartItemVO> getCheckedCartItemList(Integer userId);
}
