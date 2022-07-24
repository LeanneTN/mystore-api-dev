package com.example.mystoreapidev.controller.front;

import com.example.mystoreapidev.common.CONSTANT;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.User;
import com.example.mystoreapidev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/user/")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public CommonResponse<User> login(@RequestParam @NotBlank(message = "username can't be empty") String username,
                                      @RequestParam @NotBlank(message = "password can't be empty") String password,
                                      HttpSession session){
        CommonResponse<User> result = userService.login(username, password);
        if(result.isSuccess()){
            session.setAttribute(CONSTANT.LOGIN_USER, result.getData());
        }
        return result;
    }

    //field check
    @PostMapping("check_field")
    public CommonResponse<Object> checkField(
            @RequestParam @NotBlank(message = "field name can't be empty") String fieldName,
            @RequestParam @NotBlank(message = "field value can't be empty") String fieldValue){
        return userService.checkField(fieldName, fieldValue);
    }

    /*
    1. spring mvc uses request body to get parameters
    2. request body will complete parameter validation with @Valid
    3. MD5-> when using third party components, it has three ways:
        a. accomplished by hand(not suggested)
        b. use component in spring way
        c. use component in SpringBoot way(use @XXXX)
     */
    @PostMapping("register")
    public CommonResponse<Object> register(
            @RequestBody User user){
        return null;
    }
}
