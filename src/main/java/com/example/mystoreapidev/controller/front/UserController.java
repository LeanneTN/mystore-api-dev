package com.example.mystoreapidev.controller.front;

import com.example.mystoreapidev.common.CONSTANT;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.User;
import com.example.mystoreapidev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public CommonResponse<Object> login(@RequestParam String username, @RequestParam String password, HttpSession session){
        CommonResponse<User> result = userService.login(username, password);
        if(result.isSuccess()){
            session.setAttribute(CONSTANT.LOGIN_USER, result.getData());
        }
        return CommonResponse.createForSuccess();
    }

}
