package com.example.mystoreapidev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mystoreapidev.common.CONSTANT;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.Cart;
import com.example.mystoreapidev.domain.Order;
import com.example.mystoreapidev.domain.OrderItem;
import com.example.mystoreapidev.domain.Product;
import com.example.mystoreapidev.persistence.*;
import com.example.mystoreapidev.service.OrderService;
import com.example.mystoreapidev.utils.BigDecimalUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    public CommonResponse<Object> create(Integer userId, Integer addressId){
        //get items which are checked in cart
        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("user_id", userId).eq("checked", CONSTANT.CART.CHECKED);
        List<Cart> cartList = cartMapper.selectList(cartQueryWrapper);

        if(CollectionUtils.isEmpty(cartList)){
            return CommonResponse.createForError("cart is empty, order can't created");
        }

        //put cart item into order item
        CommonResponse<Object> cartItemToOrderItemResult = cartItemToOrderItem(cartList);

        if(cartItemToOrderItemResult.isSuccess())
            return cartItemToOrderItemResult;

        List<OrderItem> orderItemList = (List<OrderItem>) cartItemToOrderItemResult.getData();

        //calculate the total price of order
        BigDecimal paymentPrice = new BigDecimal("0");
        for(OrderItem orderItem : orderItemList){
            paymentPrice = BigDecimalUtil.add(paymentPrice.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }

        Order order = new Order();
        long orderNo = generateOrderNo();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setAddressId(addressId);
        order.setPaymentPrice(paymentPrice);
        order.setPaymentType(CONSTANT.PayType.ALIPAY.getCode());
        order.setPostage(0);
        order.setStatus(CONSTANT.OrderStatus.UNPAID.getCode());
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        int result = orderMapper.insert(order);
        if(result != 1){
            return CommonResponse.createForError("order generate failed");
        }

        return null;
    }

    private CommonResponse<Object> cartItemToOrderItem(List<Cart> cartItemList){
        List<OrderItem> orderItemList = Lists.newArrayList();

        for(Cart cartItem : cartItemList){
            Product product = productMapper.selectById(cartItem.getProductId());

            if(product == null){
                return CommonResponse.createForError("product id: "+ cartItem.getProductId() + " is wrong");
            }

            //check selling status
            if(product.getStatus() != CONSTANT.ProductStatus.ON_SALE.getCode()){
                return CommonResponse.createForError("product: " + product.getName() + " is not on sale");
            }

            //check stock
            if(product.getStock() < cartItem.getQuantity()){
                return CommonResponse.createForError("product: " + product.getName() + "'s stock is not enough");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setUserId(cartItem.getUserId());
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentPrice(product.getPrice());
            orderItem.setTotalPrice(BigDecimalUtil.multiply(product.getPrice().doubleValue(),cartItem.getQuantity()));
            orderItem.setQuantity(cartItem.getQuantity());

            orderItemList.add(orderItem);
        }

        return CommonResponse.createForSuccess(orderItemList);
    }

    private Long generateOrderNo(){
        return System.currentTimeMillis() + new Random().nextInt(1000);
    }
}
