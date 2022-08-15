package com.example.mystoreapidev.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@TableName("mystore_shipping")
@Data
public class Address {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("user_id")
    private Integer userId;
    @NotEmpty(message = "address name can't be empty")
    private String addressName;
    private String addressPhone;
    @NotEmpty(message = "mobile can't be empty")
    private String addressMobile;
    @NotEmpty(message = "province can't be empty")
    private String addressProvince;
    @NotEmpty(message = "city can't be empty")
    private String addressCity;
    @NotEmpty(message = "district can't be empty")
    private String addressDistrict;

    private String addressDetail;
    private String addressZip;
    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;
}
