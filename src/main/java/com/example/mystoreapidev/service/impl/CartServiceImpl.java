package com.example.mystoreapidev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.mystoreapidev.VO.CartVO;
import com.example.mystoreapidev.common.CONSTANT;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.Cart;
import com.example.mystoreapidev.persistence.CartMapper;
import com.example.mystoreapidev.persistence.ProductMapper;
import com.example.mystoreapidev.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service("cartService")
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    public CommonResponse<Object> addCart(Integer userId, Integer productId, Integer quantity){
        /*
        if(userId == null){
            return CommonResponse.createForError("user id can't be null");
        }
        if(quantity == null || quantity == 0){
            return CommonResponse.createForError("quantity can't be null or 0");
        }

        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("product_id", productId);
        Cart cartItem = cartMapper.selectOne(queryWrapper);

        int productStock = productMapper.selectById(productId).getStock();

        if(cartItem == null){
            cartItem = new Cart();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            if(productStock < quantity){
                quantity = productStock;
            }
            cartItem.setQuantity(quantity);
            cartItem.setChecked(CONSTANT.CART.CHECKED);
            cartItem.setCreateTime(LocalDateTime.now());
            cartItem.setUpdateTime(LocalDateTime.now());
            cartMapper.insert(cartItem);
        }else{
            if(productStock < cartItem.getQuantity() + quantity){
                quantity = productStock;
            }
            UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", cartItem.getId());
            updateWrapper.set("quantity", quantity);
            updateWrapper.set("update_time", LocalDateTime.now());
            cartMapper.update(cartItem, updateWrapper);
        }

        return CommonResponse.createForSuccess();
        */

        return null;
    }

    private CartVO getCartVOAndCheckStock(Integer userId){
        CartVO cartVO = new CartVO();
        return cartVO;
    }
}
