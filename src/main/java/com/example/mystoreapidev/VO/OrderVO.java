package com.example.mystoreapidev.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.mystoreapidev.domain.Address;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderVO {
    private Integer id;
    private Long orderNo;
    private Integer userId;

    private BigDecimal paymentPrice;
    private Integer paymentType;
    private Integer postage;
    private Integer status;

    private LocalDateTime paymentTime;
    private LocalDateTime sendTime;
    private LocalDateTime endTime;
    private LocalDateTime closeTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private Address address;
}
