package com.example.mystoreapidev.controller.front;

import com.example.mystoreapidev.VO.CartVO;
import com.example.mystoreapidev.common.CONSTANT;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.common.ResponseCode;
import com.example.mystoreapidev.domain.User;
import com.example.mystoreapidev.service.CartService;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/cart/")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("add")
    public CommonResponse<CartVO> addCart(
            @RequestParam @NotNull(message = "product id can't be null") Integer productId,
            @RequestParam @Range(min=1, message = "product quantity can't be less than 1") Integer quantity,
            HttpSession session){
        User login =(User) session.getAttribute(CONSTANT.LOGIN_USER);
        if(login == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }

        return cartService.addCart(login.getId(), productId, quantity);
    }

    @PostMapping("update")
    public CommonResponse<CartVO> updateCart(
            @RequestParam @NotNull(message = "product id can't be null") Integer productId,
            @RequestParam @Range(min = 1, message = "product quantity can't be less than 1") Integer quantity,
            HttpSession session
    ){
        User loginUser = (User) session.getAttribute(CONSTANT.LOGIN_USER);
        if(loginUser==null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }

        return cartService.updateCart(loginUser.getId(), productId, quantity);
    }
}
