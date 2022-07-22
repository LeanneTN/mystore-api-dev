package com.example.mystoreapidev;

import com.example.mystoreapidev.domain.User;
import com.example.mystoreapidev.persistence.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MystoreApiDevApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void testML(){
        System.out.println(userMapper);
        List<User> userList = userMapper.selectList(null);
        System.out.println(userList.size());
    }

}
