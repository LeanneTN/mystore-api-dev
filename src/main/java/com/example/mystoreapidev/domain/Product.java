package com.example.mystoreapidev.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.checkerframework.checker.units.qual.Time;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("mystore_product")
public class Product {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "category_id")
    private Integer categoryId;

    private String name;
    private String subtitle;

    @TableField(value = "main_image")
    private String mainImage;
    @TableField(value = "sub_images")
    private String subImages;

    private String detail;
    private BigDecimal price;
    private Integer stock;
    private Integer status;

    @TableField(value = "create_time")
    private LocalDateTime createTime;
    @TableField(value = "update_time")
    private LocalDateTime updateTime;
}
