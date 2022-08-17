package com.example.mystoreapidev.VO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCartItemVO {

    private List<OrderItemVO> orderItemVOList;
    private BigDecimal paymentPrice;
    private String imageServer;
}
