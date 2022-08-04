package com.example.mystoreapidev.controller.admin;

import com.example.mystoreapidev.VO.AliOSSPolicy;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.service.OSSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alioss/")
@Slf4j
public class AliOSSController {

    @Autowired
    private OSSService ossService;

    @Value("${aliyun.oss.accessId}")
    private String ACCESS_ID;
    @Value("${aliyun.oss.accessKey}")
    private String ACCESS_KEY;
    @Value("${aliyun.oss.endpoint")
    private String ENDPOINT;
    @Value("${aliyun.oss.bucket")
    private String BUCKET;
    @Value("${aliyun.oss.dir}")
    private String DIR;

    @GetMapping("get_policy")
    public CommonResponse<AliOSSPolicy> getPolicy(){
        String host = "http://"+BUCKET+"."+ENDPOINT;
        try{

        }catch (Exception e){
            log.info("server end generate alioss policy failed.",e);
        }
        return ossService.generatePolicy();
    }
}
