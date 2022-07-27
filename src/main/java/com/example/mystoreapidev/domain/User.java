package com.example.mystoreapidev.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import javafx.util.converter.LocalDateStringConverter;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@TableName("mystore_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @NotBlank(message = "username can't be empty")
    private String username;
    @NotBlank(message = "password can't be empty")
    private String password;
    @NotBlank(message = "email can't be empty")
    private String email;
    @NotBlank(message = "phone can't be empty")
    private String phone;

    private String question;
    private String answer;

    private Integer role;

    //localDateTime fixes original questions from Date
    //most extinguish shortcoming of Date is threads of Date is less secure
    //mybatis-plus can automatically render two similar name between POJO and database but the capability is limited
    //like: createTime -> create_time
    //use @TableField to avoid error that might happen
    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;
}
