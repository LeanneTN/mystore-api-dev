package com.example.mystoreapidev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.User;
import com.example.mystoreapidev.persistence.UserMapper;
import com.example.mystoreapidev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return CommonResponse.createForSuccess(loginUser);
    }
}
