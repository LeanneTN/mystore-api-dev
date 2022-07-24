package com.example.mystoreapidev.service;

import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.User;

public interface UserService {
    CommonResponse<User> login(String username, String password);

    CommonResponse<Object> checkField(String fieldName, String fieldValue);
}
