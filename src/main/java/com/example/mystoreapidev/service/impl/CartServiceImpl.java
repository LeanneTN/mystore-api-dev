package com.example.mystoreapidev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.mystoreapidev.VO.CartItemVO;
import com.example.mystoreapidev.VO.CartVO;
import com.example.mystoreapidev.common.CONSTANT;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.common.ResponseCode;
import com.example.mystoreapidev.domain.Cart;
import com.example.mystoreapidev.domain.Product;
import com.example.mystoreapidev.persistence.CartMapper;
import com.example.mystoreapidev.persistence.ProductMapper;
import com.example.mystoreapidev.service.CartService;
import com.example.mystoreapidev.utils.BigDecimalUtil;
import com.example.mystoreapidev.utils.ImageServerConfig;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service("cartService")
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Resource
    private ImageServerConfig imageServerConfig;

    @Override
    public CommonResponse<CartVO> addCart(Integer userId, Integer productId, Integer quantity){
        if(userId == null){
            return CommonResponse.createForError("user id can't be null");
        }
        if(productId == null){
            return CommonResponse.createForError("product id can't be null");
        }
        if(quantity == null || quantity == 0){
            return CommonResponse.createForError("quantity can't be null or 0");
        }

        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("product_id", productId);
        Cart cartItem = cartMapper.selectOne(queryWrapper);

        if(cartItem == null){
            cartItem = new Cart();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartItem.setChecked(CONSTANT.CART.CHECKED);
            cartItem.setCreateTime(LocalDateTime.now());
            cartItem.setUpdateTime(LocalDateTime.now());
            cartMapper.insert(cartItem);
        }else{
            UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", cartItem.getId());
            updateWrapper.set("quantity", quantity);
            updateWrapper.set("update_time", LocalDateTime.now());
            cartMapper.update(cartItem, updateWrapper);
        }

        CartVO cartVO = this.getCartVOAndCheckStock(userId);


        return CommonResponse.createForSuccess(cartVO);

    }

    //this function plays role in transfer the ordinary values in database into VOs to display in front page
    //At the same time, it will set the number correctly according to the numbers of products in repository
    private CartVO getCartVOAndCheckStock(Integer userId){
        CartVO cartVO = new CartVO();
        List<CartItemVO> cartItemVOList = Lists.newArrayList();

        //从数据库中查询userId的购物车信息
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Cart> cartItemList = cartMapper.selectList(queryWrapper);

        BigDecimal cartTotalPrice = new BigDecimal("0");
        boolean allSelected = true;

        if(CollectionUtils.isNotEmpty(cartItemList)){
            for(Cart cartItem : cartItemList){
                CartItemVO cartItemVO = new CartItemVO();
                cartItemVO.setId(cartItem.getId());
                cartItemVO.setUserId(cartItem.getUserId());
                cartItemVO.setProductId(cartItem.getProductId());
                //cartItemVO.setQuantity(cartItem.getQuantity());
                cartItemVO.setChecked(cartItem.getChecked());
                cartItemVO.setCartItemTotalPrice(BigDecimal.valueOf(0));

                Product product = productMapper.selectById(cartItem.getProductId());
                if(product != null){
                    cartItemVO.setProductName(product.getName());
                    cartItemVO.setProductSubtitle(product.getSubtitle());
                    cartItemVO.setProductMainImage(product.getMainImage());
                    cartItemVO.setProductPrice(product.getPrice());
                    cartItemVO.setProductStock(product.getStock());

                    //处理库存
                    if(product.getStock() >= cartItem.getQuantity()){
                        cartItemVO.setQuantity(cartItem.getQuantity());
                        cartItemVO.setCheckStock(true);
                    }
                    else{
                        cartItemVO.setQuantity(product.getStock());
                        Cart updateStockCart = new Cart();
                        UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
                        updateWrapper.eq("id", cartItem.getId());
                        updateWrapper.set("quantity", product.getStock());
                        updateWrapper.set("update_time", LocalDateTime.now());
                        cartMapper.update(updateStockCart, updateWrapper);
                        cartItemVO.setCheckStock(false);
                    }
                    cartItemVO.setCartItemTotalPrice(
                            BigDecimalUtil.multiply(cartItemVO.getProductPrice().doubleValue(),cartItemVO.getQuantity().doubleValue()));
                }
                cartItemVOList.add(cartItemVO);

                if(cartItem.getChecked() == CONSTANT.CART.CHECKED){
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartItemVO.getCartItemTotalPrice().doubleValue());
                }else{
                    allSelected = false;
                }
            }
            cartVO.setCartItemVOList(cartItemVOList);
            cartVO.setCartTotalPrice(cartTotalPrice);
            cartVO.setAllSelected(allSelected);
            cartVO.setProductImageServer(imageServerConfig.getUrl());
        }

        return cartVO;
    }

    @Override
    public CommonResponse<CartVO> updateCart(Integer userId, Integer productId, Integer quantity){
        if(userId == null){
            return CommonResponse.createForError("user id can't be null");
        }
        if(quantity == null || quantity == 0){
            return CommonResponse.createForError("quantity can't be null or 0");
        }
        if(productId == null){
            return CommonResponse.createForError("product id can't be null");
        }

        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("product_id", productId);
        Cart cartItem = cartMapper.selectOne(queryWrapper);

        if(cartItem!= null){
            UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", cartItem.getId());
            updateWrapper.set("quantity", quantity);
            updateWrapper.set("update_time", LocalDateTime.now());
            cartMapper.update(cartItem, updateWrapper);

            CartVO cartVO = this.getCartVOAndCheckStock(userId);

            return CommonResponse.createForSuccess(cartVO);
        }else{
            return CommonResponse.createForError(ResponseCode.ARGUMENT_ILLEGAL.getCode(), ResponseCode.ARGUMENT_ILLEGAL.getDescription());
        }
    }

    //productIds: "1,2,3..."
    @Override
    public CommonResponse<CartVO> deleteCartItems(Integer userId, String productIds){
        if(userId==null){
            return CommonResponse.createForError("please login before send request");
        }
        if(productIds==null){
            return CommonResponse.createForError("product ids can't be null");
        }
        List<String> productIdStrings = Splitter.on(",").splitToList(productIds);
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        for(String productIdString : productIdStrings){
            int productId = Integer.parseInt(productIdString);
            queryWrapper.eq("user_id", userId).eq("product_id", productId);
            cartMapper.delete(queryWrapper);
            queryWrapper.clear();
        }
        CartVO cartVO = this.getCartVOAndCheckStock(userId);

        return CommonResponse.createForSuccess(cartVO);
    }

    @Override
    public CommonResponse<CartVO> list(Integer userId){
        if(userId==null){
            return CommonResponse.createForError("please login before send request");
        }
        CartVO cartVO = this.getCartVOAndCheckStock(userId);
        return CommonResponse.createForSuccess(cartVO);
    }

    @Override
    public CommonResponse<Integer> getCartCount(Integer userId){
        if(userId==null){
            return CommonResponse.createForError("please login before send request");
        }
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Cart> cartList = cartMapper.selectList(queryWrapper);
        int count = 0;
        for (Cart item : cartList){
            count += item.getQuantity();
        }
        return CommonResponse.createForSuccess(count);
    }

    @Override
    public CommonResponse<CartVO> updateAllCheckStatus(Integer userId, Integer checkStatus){
        if(userId==null){
            return CommonResponse.createForError("please login before send request");
        }

        if(checkStatus==null){
            return CommonResponse.createForError("check status can't be null");
        }

        Cart cartItem = new Cart();
        UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();


        updateWrapper.eq("user_id", userId);
        updateWrapper.set("checked", checkStatus);
        updateWrapper.set("update_time", LocalDateTime.now());
        cartMapper.update(cartItem, updateWrapper);

        CartVO cartVO = this.getCartVOAndCheckStock(userId);
        return CommonResponse.createForSuccess(cartVO);
    }

    @Override
    public CommonResponse<CartVO> updateCheckStatus(Integer userId, Integer productId, Integer checkStatus){
        if(userId==null){
            return CommonResponse.createForError("please login before send request");
        }
        if (productId==null){
            return CommonResponse.createForError("product id can't be null");
        }
        if(checkStatus==null){
            return CommonResponse.createForError("check status can't be null");
        }
        Cart cartItem = new Cart();
        UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId);
        updateWrapper.eq("product_id", productId);
        updateWrapper.set("checked", checkStatus);
        updateWrapper.set("update_time", LocalDateTime.now());

        CartVO cartVO = this.getCartVOAndCheckStock(userId);
        return CommonResponse.createForSuccess(cartVO);
    }
}
