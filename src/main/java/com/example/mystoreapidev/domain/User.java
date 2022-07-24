package com.example.mystoreapidev.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import javafx.util.converter.LocalDateStringConverter;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mystore_user")
public class User {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String phone;

    private String question;
    private String answer;

    private Integer role;

    //localDateTime fixes original questions from Date
    //most extinguish shortcoming of Date is threads of Date is less secure
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
