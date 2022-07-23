package com.example.mystoreapidev.controller.front;

import com.example.mystoreapidev.common.CommonResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
public class UserController {

    @PostMapping("login")
    public CommonResponse<Object> login(){
        return CommonResponse.createForSuccess();
    }

}
