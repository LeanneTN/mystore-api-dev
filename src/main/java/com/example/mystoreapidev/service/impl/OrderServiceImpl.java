package com.example.mystoreapidev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mystoreapidev.VO.OrderCartItemVO;
import com.example.mystoreapidev.VO.OrderItemVO;
import com.example.mystoreapidev.VO.OrderVO;
import com.example.mystoreapidev.common.CONSTANT;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.*;
import com.example.mystoreapidev.persistence.*;
import com.example.mystoreapidev.service.AddressService;
import com.example.mystoreapidev.service.OrderService;
import com.example.mystoreapidev.utils.BigDecimalUtil;
import com.example.mystoreapidev.utils.DateTimeFormatterUtil;
import com.example.mystoreapidev.utils.ImageServerConfig;
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

    @Autowired
    private AddressService addressService;

    @Autowired
    private ImageServerConfig imageServerConfig;

    @Override
    public CommonResponse<OrderVO> create(Integer userId, Integer addressId){
        //get items which are checked in cart
        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("user_id", userId).eq("checked", CONSTANT.CART.CHECKED);
        List<Cart> cartList = cartMapper.selectList(cartQueryWrapper);

        if(CollectionUtils.isEmpty(cartList)){
            return CommonResponse.createForError("cart is empty, order can't created");
        }

        //put cart item into order item
//        CommonResponse<OrderVO> cartItemToOrderItemResult = cartItemToOrderItem(cartList);
//
//        if(cartItemToOrderItemResult.isSuccess())
//            return cartItemToOrderItemResult;

        CommonResponse cartItemToOrderItemResult = this.cartItemToOrderItem(cartList);
        if(!cartItemToOrderItemResult.isSuccess()){
            return cartItemToOrderItemResult;
        }

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

        //todo:change it into batch insert
        for(OrderItem orderItem : orderItemList){
            orderItem.setOrderNo(orderNo);
            orderItem.setCreateTime(LocalDateTime.now());
            orderItem.setUpdateTime(LocalDateTime.now());
            orderItemMapper.insert(orderItem);
        }

        for(OrderItem orderItem : orderItemList){
            Product product = productMapper.selectById(orderItem.getProductId());
            product.setStock(product.getStock() - orderItem.getQuantity());
            productMapper.updateById(product);
        }

        for(Cart cartItem : cartList){
            cartMapper.deleteById(cartItem.getId());
        }

        OrderVO orderVO = generateOrderVO(order, orderItemList);

        if(orderVO == null){
            return CommonResponse.createForError("address id is wrong");
        }

        return CommonResponse.createForSuccess(orderVO);
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

    @Override
    public CommonResponse<OrderCartItemVO> getCheckedCartItemList(Integer userId){
        OrderCartItemVO orderCartItemVO = new OrderCartItemVO();

        //1. get selected product from cart
        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("user_id", userId);
        cartQueryWrapper.eq("checked", CONSTANT.CART.CHECKED);
        List<Cart> cartItemList = cartMapper.selectList(cartQueryWrapper);

        if(CollectionUtils.isEmpty(cartItemList)){
            return CommonResponse.createForError("购物车为空");
        }

        //2. put cartItem into orderItem
        CommonResponse cartItemToOrderItemResult = this.cartItemToOrderItem(cartItemList);
        if(!cartItemToOrderItemResult.isSuccess()){
            return cartItemToOrderItemResult;
        }

        List<OrderItem> orderItemList = (List<OrderItem>)cartItemToOrderItemResult.getData();

        //3. total price and OrderItem->OrderItemVO
        List<OrderItemVO> orderItemVOList = Lists.newArrayList();
        BigDecimal paymentPrice = new BigDecimal("0");
        for(OrderItem orderItem : orderItemList){
            paymentPrice = BigDecimalUtil.add(paymentPrice.doubleValue(),orderItem.getTotalPrice().doubleValue());
            orderItemVOList.add(orderItemToOrderItemVO(orderItem));
        }

        orderCartItemVO.setOrderItemVOList(orderItemVOList);
        orderCartItemVO.setPaymentPrice(paymentPrice);
        orderCartItemVO.setImageServer(imageServerConfig.getUrl());

        return CommonResponse.createForSuccess(orderCartItemVO);
    }

    @Override
    public CommonResponse<OrderVO> getOrderDetail(Integer userId, Long orderNo){
        if(userId == null){
            return CommonResponse.createForError("user id can't be null");
        }

        if(orderNo == null){
            return CommonResponse.createForError("order no can't be null");
        }
        OrderVO orderVO = null;

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);

        Order order = orderMapper.selectOne(queryWrapper);

        if(order == null)
            return CommonResponse.createForError("order is not exists");

        QueryWrapper<OrderItem> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("user_id", userId).eq("order_no", orderNo);
        List<OrderItem> orderItemList = orderItemMapper.selectList(queryWrapper1);

        orderVO = generateOrderVO(order, orderItemList);

        return CommonResponse.createForSuccess(orderVO);
    }

    @Override
    public CommonResponse<Page<OrderVO>> getOrderList(Integer userId, int pageNum, int pageSize) {
        Page<Order> result = new Page<>();
        result.setCurrent(pageNum);
        result.setSize(pageSize);

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);

        result = orderMapper.selectPage(result, queryWrapper);

        List<Order> orderList = result.getRecords();

        List<OrderVO> orderVOList = Lists.newArrayList();

        // according to selected order to get the specific items in each order
        for(Order order : orderList){
            QueryWrapper<OrderItem> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("user_id", userId).eq("order_no", order.getOrderNo());

            List<OrderItem> orderItemList = orderItemMapper.selectList(queryWrapper1);

            OrderVO orderVO = generateOrderVO(order, orderItemList);

            orderVOList.add(orderVO);
        }

        Page<OrderVO> newResult = new Page<>();
        newResult.setCurrent(result.getCurrent());
        newResult.setSize(result.getSize());
        newResult.setTotal(result.getTotal());
        newResult.setRecords(orderVOList);
        return CommonResponse.createForSuccess(newResult);
    }

    @Override
    public CommonResponse<String> cancelOrder(Integer userId, Long orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("order_no", orderNo);
        Order order = orderMapper.selectOne(queryWrapper);

        if(order == null){
            return CommonResponse.createForError("order is not exists");
        }
        if(order.getStatus() != CONSTANT.OrderStatus.UNPAID.getCode()){
            return CommonResponse.createForError("order is not under the state of waiting for paid, can't be cancel");
        }
        order.setStatus(CONSTANT.OrderStatus.CANCEL.getCode());
        int result = orderMapper.updateById(order);
        if(result != 1){
            return CommonResponse.createForError("cancel order failed");
        }
        return CommonResponse.createForSuccess();
    }

    private Long generateOrderNo(){
        return System.currentTimeMillis() + new Random().nextInt(1000);
    }

    private OrderVO generateOrderVO(Order order, List<OrderItem> orderItemList){
        OrderVO orderVO = new OrderVO();

        orderVO.setId(order.getId());
        orderVO.setOrderNo(order.getOrderNo());
        orderVO.setUserId(order.getUserId());
        orderVO.setPaymentPrice(order.getPaymentPrice());
        orderVO.setPaymentType(order.getPaymentType());
        orderVO.setPostage(order.getPostage());
        orderVO.setStatus(order.getStatus());

        orderVO.setPaymentTime(DateTimeFormatterUtil.format(order.getPaymentTime()));
        orderVO.setSendTime(DateTimeFormatterUtil.format(order.getSendTime()));
        orderVO.setEndTime(DateTimeFormatterUtil.format(order.getEndTime()));
        orderVO.setCloseTime(DateTimeFormatterUtil.format(order.getCloseTime()));
        orderVO.setCreateTime(DateTimeFormatterUtil.format(order.getCreateTime()));
        orderVO.setUpdateTime(DateTimeFormatterUtil.format(order.getUpdateTime()));

        //todo: invoke addressService.getById()  it shouldn't invoke addressMapper and addressService at same time
        Address address = addressMapper.selectById(order.getAddressId());
        if(address == null)
            return null;
        orderVO.setAddressVO(addressService.addressToAddressVO(address));

        List<OrderItemVO> orderItemVOList = Lists.newArrayList();

        for(OrderItem orderItem : orderItemList){
            orderItemVOList.add(orderItemToOrderItemVO(orderItem));
        }
        orderVO.setOrderItemVOList(orderItemVOList);
        orderVO.setImageServer(imageServerConfig.getUrl());

        return orderVO;
    }

    private OrderItemVO orderItemToOrderItemVO(OrderItem orderItem){
        OrderItemVO orderItemVO = new OrderItemVO();
        orderItemVO.setId(orderItem.getId());
        orderItemVO.setProductId(orderItem.getProductId());
        orderItemVO.setProductName(orderItem.getProductName());
        orderItemVO.setProductImage(orderItem.getProductImage());
        orderItemVO.setCurrentPrice(orderItem.getCurrentPrice());
        orderItemVO.setQuantity(orderItem.getQuantity());
        orderItemVO.setTotalPrice(orderItem.getTotalPrice());
        return orderItemVO;
    }
}
