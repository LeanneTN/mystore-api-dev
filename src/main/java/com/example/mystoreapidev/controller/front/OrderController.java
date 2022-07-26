package com.example.mystoreapidev.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mystoreapidev.VO.OrderCartItemVO;
import com.example.mystoreapidev.VO.OrderVO;
import com.example.mystoreapidev.common.CONSTANT;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.common.ResponseCode;
import com.example.mystoreapidev.domain.User;
import com.example.mystoreapidev.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/order/")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("create")
    public CommonResponse<OrderVO> create(@RequestParam @NotNull(message = "address id can't be null") Integer addressId,
                                          HttpSession session){
        User login =(User) session.getAttribute(CONSTANT.LOGIN_USER);
        if(login == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return orderService.create(login.getId(), addressId);
    }

    @GetMapping("cart_item_list")
    public CommonResponse<OrderCartItemVO> getCheckedCartItemList(HttpSession session){
        User login =(User) session.getAttribute(CONSTANT.LOGIN_USER);
        if(login == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return orderService.getCheckedCartItemList(login.getId());
    }

    @GetMapping("detail")
    public CommonResponse<OrderVO> getOrderDetail(
            @RequestParam @NotNull(message = "order no can't be null") Long orderNo,
            HttpSession session
    ){
        User login =(User) session.getAttribute(CONSTANT.LOGIN_USER);
        if(login == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return orderService.getOrderDetail(login.getId(), orderNo);
    }

    @GetMapping("list")
    public CommonResponse<Page<OrderVO>> getOrderList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "2") int pageSize,
            HttpSession session
    ){
        User login =(User) session.getAttribute(CONSTANT.LOGIN_USER);
        if(login == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return orderService.getOrderList(login.getId(), pageNum, pageSize);
    }

    @GetMapping("cancel")
    public CommonResponse<String> cancel(
            @RequestParam @NotNull(message = "order no can't be null") Long orderNo,
            HttpSession session
    ){
        User login =(User) session.getAttribute(CONSTANT.LOGIN_USER);
        if(login == null){
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return orderService.cancelOrder(login.getId(), orderNo);
    }
}
