package com.example.mystoreapidev.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.mystoreapidev.common.CONSTANT;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.domain.User;
import com.example.mystoreapidev.persistence.UserMapper;
import com.example.mystoreapidev.service.UserService;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        User loginUser = userMapper.selectOne(Wrappers.<User>query().eq("username", username));
        if(loginUser==null)
            return CommonResponse.createForError("wrong username or password");
        boolean checkPassword = bCryptPasswordEncoder.matches(password, loginUser.getPassword());
        loginUser.setPassword(StringUtils.EMPTY);
        return checkPassword?CommonResponse.createForSuccess(loginUser):CommonResponse.createForError("wrong username or password");
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
        CommonResponse<Object> checkResult = checkField("username", username);
        if(checkResult.isSuccess()){
            return CommonResponse.createForError("username is not exists");
        }
        // get token from local cache
        String token = localCache.getIfPresent(username);
        log.info("Get token from LocalCache : ({}, {})", username, token);
        if(StringUtils.isBlank(token)){
            return CommonResponse.createForError("token is not available or out of date");
        }
        if(StringUtils.equals(token, forgetToken)){
            String md5Password = bCryptPasswordEncoder.encode(newPassword);

            User user = new User();
            user.setPassword(md5Password);
            user.setUsername(username);

            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("username", username);
            updateWrapper.set("password", md5Password);
            int rows = userMapper.update(user, updateWrapper);

            if(rows > 0){
                return CommonResponse.createForSuccess();
            }

            return CommonResponse.createForError("failed to reset password by answer question, try later");
        }
        else{
            return CommonResponse.createForError("wrong token, please get token again later");
        }
    }

    @Override
    public CommonResponse<User> getUserDetail(Integer userId) {
        User user = userMapper.selectById(userId);
        if(user == null){
            return CommonResponse.createForError("user information can't find");
        }
        user.setPassword(StringUtils.EMPTY);
        return CommonResponse.createForSuccess(user);
    }

    @Override
    public CommonResponse<Object> resetPassword(String oldPassword, String newPassword, User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", user.getId());
        //queryWrapper.eq("password", bCryptPasswordEncoder.encode(oldPassword));
        long rows = userMapper.selectCount(queryWrapper);
        User user1 = userMapper.selectOne(queryWrapper);
        if(!bCryptPasswordEncoder.matches(oldPassword, user1.getPassword())){
            return CommonResponse.createForError("wrong old password");
        }
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", user.getId());

        rows = userMapper.update(user, updateWrapper);

        if(rows > 0){
            return CommonResponse.createForSuccess();
        }
        return CommonResponse.createForError("reset password failed");
    }

    @Override
    public CommonResponse<Object> updateUserInfo(User user) {
        CommonResponse<Object> checkResult = checkField("email", user.getEmail());
        if(!checkResult.isSuccess())
            return checkResult;
        checkResult = checkField("phone", user.getPhone());
        if(!checkResult.isSuccess())
            return checkResult;

        user.setUpdateTime(LocalDateTime.now());
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", user.getId());
        updateWrapper.set("email", user.getEmail());
        updateWrapper.set("phone", user.getPhone());
        updateWrapper.set("question", user.getQuestion());
        updateWrapper.set("answer", user.getAnswer());
        updateWrapper.set("update_time", user.getUpdateTime());
        long rows = userMapper.update(user, updateWrapper);

        if(rows > 0){
            return CommonResponse.createForSuccess();
        }
        return CommonResponse.createForError("reset user information failed");
    }
}
