package com.example.mystoreapidev.service;

import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.User;

public interface UserService {
    CommonResponse<User> login(String username, String password);

    CommonResponse<Object> checkField(String fieldName, String fieldValue);

    CommonResponse<Object> register(User user);

    CommonResponse<String> getForgetQuestion(String username);

    CommonResponse<String> checkForgetQuestion(String username, String question, String answer);

    CommonResponse<Object> resetForgetPassword(String username, String newPassword, String forgetToken);

    CommonResponse<User> getUserDetail(Integer userId);

    CommonResponse<Object> resetPassword(String oldPassword, String newPassword, User user);

    CommonResponse<Object> updateUserInfo(User user);
}
