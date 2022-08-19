package com.example.mystoreapidev.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mystoreapidev.VO.OrderCartItemVO;
import com.example.mystoreapidev.VO.OrderVO;
import com.example.mystoreapidev.common.CommonResponse;

public interface OrderService {
    CommonResponse<OrderVO> create(Integer userId, Integer addressId);

    CommonResponse<OrderCartItemVO> getCheckedCartItemList(Integer userId);

    CommonResponse<OrderVO> getOrderDetail(Integer userId, Long orderNo);

    CommonResponse<Page<OrderVO>> getOrderList(Integer userId, int pageNum, int pageSize);

    CommonResponse<String> cancelOrder(Integer userId, Long orderNo);
}
