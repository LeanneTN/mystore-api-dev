package com.example.mystoreapidev.service;

import com.example.mystoreapidev.VO.AliOSSPolicy;
import com.example.mystoreapidev.common.CommonResponse;

public interface OSSService {
    CommonResponse<AliOSSPolicy> generatePolicy();
}
