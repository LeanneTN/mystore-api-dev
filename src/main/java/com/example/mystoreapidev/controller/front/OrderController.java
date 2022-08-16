package com.example.mystoreapidev.controller.front;

import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/order/")
public class OrderController {
    @Autowired
    private OrderService orderService;

    public CommonResponse<Object> create(@RequestParam @NotNull(message = "address id can't be null") Integer addressId,
                                         HttpSession session){
        return null;
    }
}
