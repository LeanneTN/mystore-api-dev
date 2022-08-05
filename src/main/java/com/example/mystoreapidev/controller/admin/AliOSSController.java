package com.example.mystoreapidev.controller.admin;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.example.mystoreapidev.VO.AliOSSCallBackResult;
import com.example.mystoreapidev.VO.AliOSSPolicy;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.service.OSSService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@RestController
@RequestMapping("/alioss/")
@Slf4j
public class AliOSSController {

    @Autowired
    private OSSService ossService;

    @GetMapping("get_policy")
    public CommonResponse<AliOSSPolicy> getPolicy(){
        return ossService.generatePolicy();
    }

    @PostMapping("callback")
    public CommonResponse<AliOSSCallBackResult> callback(HttpServletRequest request){
        return ossService.callback(request);
    }
}
