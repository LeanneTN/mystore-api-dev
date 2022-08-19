package com.example.mystoreapidev.service;

import com.example.mystoreapidev.VO.QRCodeVO;
import com.example.mystoreapidev.common.CommonResponse;

public interface MyAlipayService {
    CommonResponse<QRCodeVO> getQRCode(Integer userId, Long orderNo);
}
