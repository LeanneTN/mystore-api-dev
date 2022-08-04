package com.example.mystoreapidev.service.impl;

import com.aliyun.oss.OSSClient;
import com.example.mystoreapidev.VO.AliOSSPolicy;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.service.OSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ossService")
public class AliOSSServiceImpl implements OSSService {

    @Autowired
    private OSSClient ossClient;

    @Override
    public CommonResponse<AliOSSPolicy> generatePolicy() {
        System.out.println("aaa");
        System.out.println(ossClient.getBucketInfo("mystore-demo").getBucket().getLocation());
        return null;
    }
}
