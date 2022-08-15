package com.example.mystoreapidev;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mystoreapidev.domain.Product;
import com.example.mystoreapidev.domain.User;
import com.example.mystoreapidev.persistence.ProductMapper;
import com.example.mystoreapidev.persistence.UserMapper;
import com.example.mystoreapidev.utils.DateTimeFormatterUtil;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class MystoreApiDevApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void testML(){
        System.out.println(userMapper);
        List<User> userList = userMapper.selectList(null);
        System.out.println(userList.size());
    }

    @Test
    public void testMPPage(){
        Page<Product> result = new Page<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("name", "%%");
        result = productMapper.selectPage(result, queryWrapper);
        System.out.println("aaa");
    }

}
