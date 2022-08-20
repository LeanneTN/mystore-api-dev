package com.example.mystoreapidev.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("mystore_pay_info")
@Data
public class PayInfo {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("user_id")
    private Integer userId;
    @TableField("order_no")
    private Long orderNo;
    @TableField("payment_type")
    private Integer paymentType;
    @TableField("trade_no")
    private String tradeNo;
    @TableField("trade_status")
    private String tradeStatus;
    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;
}
