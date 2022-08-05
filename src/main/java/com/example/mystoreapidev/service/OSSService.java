package com.example.mystoreapidev.service;

import com.example.mystoreapidev.VO.AliOSSCallBackResult;
import com.example.mystoreapidev.VO.AliOSSPolicy;
import com.example.mystoreapidev.common.CommonResponse;

import javax.servlet.http.HttpServletRequest;

public interface OSSService {
    CommonResponse<AliOSSPolicy> generatePolicy();

    public CommonResponse<AliOSSCallBackResult> callback(HttpServletRequest request);
}
