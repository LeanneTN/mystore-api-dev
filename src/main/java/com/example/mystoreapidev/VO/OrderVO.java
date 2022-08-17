package com.example.mystoreapidev.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.mystoreapidev.domain.Address;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderVO {
    private Integer id;
    private Long orderNo;
    private Integer userId;

    private BigDecimal paymentPrice;
    private Integer paymentType;
    private Integer postage;

    private Integer status;

    private String paymentTime;
    private String sendTime;
    private String endTime;
    private String closeTime;
    private String createTime;
    private String updateTime;

    private AddressVO addressVO;

    private List<OrderItemVO> orderItemVOList;

    private String imageServer;
}
