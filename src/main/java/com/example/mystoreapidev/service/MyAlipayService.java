package com.example.mystoreapidev.service;

import com.example.mystoreapidev.VO.QRCodeVO;
import com.example.mystoreapidev.common.CommonResponse;

import java.util.Map;

public interface MyAlipayService {
    CommonResponse<QRCodeVO> getQRCode(Integer userId, Long orderNo);

    CommonResponse<Object> alipayCallback(Map<String, String> params);
}
