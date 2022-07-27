package com.example.mystoreapidev.controller.front;

import com.example.mystoreapidev.common.CONSTANT;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.User;
import com.example.mystoreapidev.dto.UpdateUserDTO;
import com.example.mystoreapidev.service.UserService;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
            @RequestBody @Valid User user){
        return userService.register(user);
    }

    @PostMapping("get_forget_question")
    public CommonResponse<String> getForgetQuestion(
            @RequestParam @NotBlank(message = "username can't be empty") String username){
        return userService.getForgetQuestion(username);
    }

    @PostMapping("check_forget_anwser")
    public CommonResponse<String> checkForgetAnswer(
            @RequestParam @NotBlank(message = "username can't be empty") String username,
            @RequestParam @NotBlank(message = "question can't be empty") String question,
            @RequestParam @NotBlank(message = "answer can't be empty") String answer
    ){
        return userService.checkForgetQuestion(username, question, answer);
    }

    @PostMapping("reset_forget_password")
    public CommonResponse<Object> resetForgetPassword(
            @RequestParam @NotBlank(message = "username can't be empty") String username,
            @RequestParam @NotBlank(message = "new password can't be empty") String newPassword,
            @RequestParam @NotBlank(message = "token can't be empty") String forgetToken
    ){
        return userService.resetForgetPassword(username, newPassword, forgetToken);
    }

    @PostMapping("get_user_detail")
    public CommonResponse<User> getUserDetail(HttpSession session){
        User loginUser = (User) session.getAttribute(CONSTANT.LOGIN_USER);
        if(loginUser == null){
            return CommonResponse.createForError("user doesn't login");
        }
        return userService.getUserDetail(loginUser.getId());
    }

    @PostMapping("reset_password")
    public CommonResponse<Object> resetPassword(
            @RequestParam @NotBlank(message = "old password can't be empty") String oldPassword,
            @RequestParam @NotBlank(message = "new password can't be empty") String newPassword,
            HttpSession session
    ){
        User loginUser = (User) session.getAttribute(CONSTANT.LOGIN_USER);
        if(loginUser==null)
            return CommonResponse.createForError("user doesn't login");
        return userService.resetPassword(oldPassword, newPassword, loginUser);
    }

    @PostMapping("update_user_info")
    public CommonResponse<Object> updateUserInfo(
            @RequestBody @Valid UpdateUserDTO updateUserDTO,
            HttpSession session
    ){
        User loginUser = (User) session.getAttribute(CONSTANT.LOGIN_USER);
        if(loginUser == null){
            return CommonResponse.createForError("user doesn't login");
        }
        loginUser.setEmail(updateUserDTO.getEmail());
        loginUser.setPhone(updateUserDTO.getPhone());
        loginUser.setQuestion(updateUserDTO.getQuestion());
        loginUser.setAnswer(updateUserDTO.getAnswer());

        CommonResponse<Object> result = userService.updateUserInfo(loginUser);
        if(result.isSuccess()){
            loginUser = userService.getUserDetail(loginUser.getId()).getData();
            session.setAttribute(CONSTANT.LOGIN_USER, loginUser);
            return CommonResponse.createForSuccess();
        }
        return CommonResponse.createForError(result.getMessage());
    }

    @GetMapping("logout")
    public CommonResponse<Object> logout(HttpSession session){
        session.removeAttribute(CONSTANT.LOGIN_USER);
        return CommonResponse.createForSuccess("logout successfully");
    }
}
