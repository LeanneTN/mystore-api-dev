package com.example.mystoreapidev.VO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDetailVO {
    private Integer id;
    private Integer categoryId;
    private String name;
    private String subtitle;
    private String mainImage;
    private String subImages;
    private BigDecimal price;
    private Integer stock;
    private Integer status;
    private String createTime;
    private String updateTime;

    private Integer parentCategoryId;
    //root area of image server
    private String imageServer;
}
