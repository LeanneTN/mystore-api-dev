package com.example.mystoreapidev.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.mystoreapidev.common.CONSTANT;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.User;
import com.example.mystoreapidev.persistence.UserMapper;
import com.example.mystoreapidev.service.UserService;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.nio.file.Watchable;
import java.time.LocalDateTime;
import java.util.UUID;

@Service("userService") //name inside bracket make it faster when create an object names userService
@Slf4j //inject log into class
public class UserServiceImpl implements UserService {

    @Autowired
    //报错是java编译期和运行时的问题
    private UserMapper userMapper;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    private Cache<String, String> localCache;

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

    @Override
    public CommonResponse<Object> register(User user) {
        CommonResponse<Object> checkResult = checkField("username", user.getUsername());
        //username exists
        if(!checkResult.isSuccess()){
            return checkResult;
        }
        checkResult = checkField("email", user.getEmail());
        //email exists
        if(!checkResult.isSuccess()){
            return checkResult;
        }
        checkResult = checkField("phone", user.getPhone());
        //phone exists
        if(!checkResult.isSuccess()){
            return checkResult;
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole(CONSTANT.ROLE.CUSTOMER);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        int rows = userMapper.insert(user);
        if(rows == 0){
            return CommonResponse.createForError("register failed");
        }
        return CommonResponse.createForSuccess();
    }

    @Override
    public CommonResponse<String> getForgetQuestion(String username) {
        CommonResponse<Object> checkResult = checkField("username", username);
        if(checkResult.isSuccess()){
            return CommonResponse.createForError("username is not exists");
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);
        String question = userMapper.selectOne(Wrappers.<User>query().eq("username", username)).getQuestion();
        if(StringUtils.isNotBlank(question)){
            return CommonResponse.createForSuccess(question);
        }
        return CommonResponse.createForError("question not be set");
    }

    @Override
    public CommonResponse<String> checkForgetQuestion(String username, String question, String answer) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username).eq("question", question).eq("answer", answer);
        long rows = userMapper.selectCount(queryWrapper);
        if(rows>0){
            //use UUID to generate token string
            String forgetToken = UUID.randomUUID().toString();
            //put token into local cache, use username as key and token as value. set token available in 5 min
            localCache.put(username, forgetToken);
            log.info("Put into LocalCache: ({}, {}), {}", username, forgetToken, LocalDateTime.now());
            return CommonResponse.createForSuccess(forgetToken);
        }
        return CommonResponse.createForError("wrong answer");
    }

    @Override
    public CommonResponse<Object> resetForgetPassword(String username, String newPassword, String forgetToken) {
        return null;
    }

    @Override
    public CommonResponse<User> getUserDetail(Integer userId) {
        return null;
    }

    @Override
    public CommonResponse<Object> resetPassword(String oldPassword, String newPassword, User user) {
        return null;
    }

    @Override
    public CommonResponse<Object> updateUserInfo(User user) {
        return null;
    }
}
