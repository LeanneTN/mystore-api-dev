package com.example.mystoreapidev.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.User;
import com.example.mystoreapidev.persistence.UserMapper;
import com.example.mystoreapidev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Watchable;

@Service("userService") //name inside bracket make it faster when create an object names userService
public class UserServiceImpl implements UserService {

    @Autowired
    //报错是java编译期和运行时的问题
    private UserMapper userMapper;

    @Override
    public CommonResponse<User> login(String username, String password) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);
        queryWrapper.eq("password", password);
        //todo password needs encryption by md5
        User loginUser = userMapper.selectOne(queryWrapper);

        if(loginUser==null){
            return CommonResponse.createForError("wrong username or password");
        }
        // block password in response body
        loginUser.setPassword(null);
        return CommonResponse.createForSuccess(loginUser);
    }

    @Override
    public CommonResponse<Object> checkField(String fieldName, String fieldValue) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(StringUtils.equals(fieldName, "username")){
            queryWrapper.eq("username", fieldValue);
            long rows = userMapper.selectCount(queryWrapper);
            queryWrapper.clear();
            if(rows>0)
                return CommonResponse.createForError("username is exists");
        }
        else if(StringUtils.equals(fieldName, "phone")){
            queryWrapper.eq("phone", fieldValue);
            long rows = userMapper.selectCount(queryWrapper);
            queryWrapper.clear();
            if(rows>0)
                return CommonResponse.createForError("phone number is exists");
        }
        else if(StringUtils.equals(fieldName, "email")){
            queryWrapper.eq("email", fieldValue);
            long rows = userMapper.selectCount(queryWrapper);
            queryWrapper.clear();
            if(rows>0)
                return CommonResponse.createForError("email address is exists");
        }
        else
            return CommonResponse.createForError("parameters exception");

        return CommonResponse.createForSuccess();
    }
}
