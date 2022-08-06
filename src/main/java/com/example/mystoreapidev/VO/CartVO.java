package com.example.mystoreapidev.VO;

import java.math.BigDecimal;
import java.util.List;

public class CartVO {
    private List<CartItemVO> cartItemVOList;
    private BigDecimal cartTotalPrice;
    private Boolean allSelected;
    private String productImageServer;
}
