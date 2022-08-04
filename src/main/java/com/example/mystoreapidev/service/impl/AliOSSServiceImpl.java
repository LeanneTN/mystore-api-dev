package com.example.mystoreapidev.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.example.mystoreapidev.VO.AliOSSPolicy;
import com.example.mystoreapidev.common.CommonResponse;
import com.example.mystoreapidev.service.OSSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service("ossService")
@Slf4j
public class AliOSSServiceImpl implements OSSService {

    @Autowired
    private OSSClient ossClient;

    @Value("${aliyun.oss.accessId}")
    private String ACCESS_ID;
    @Value("${aliyun.oss.accessKey}")
    private String ACCESS_KEY;
    @Value("${aliyun.oss.endpoint}")
    private String ENDPOINT;
    @Value("${aliyun.oss.bucket}")
    private String BUCKET;
    @Value("${aliyun.oss.dir}")
    private String DIR;

    @Value("${aliyun.oss.policy.expire}")
    private Long EXPIRE;
    @Value("${aliyun.oss.policy.maxSize}")
    private long MAX_SIZE;

    @Override
    public CommonResponse<AliOSSPolicy> generatePolicy() {
        AliOSSPolicy aliOSSPolicy = new AliOSSPolicy();
        String host = "http://"+BUCKET+"."+ENDPOINT;
        try{
            long expireTime = EXPIRE;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConditions = new PolicyConditions();
            policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1024*1024*MAX_SIZE);
            policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, DIR);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConditions);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            aliOSSPolicy.setAccessId(ACCESS_ID);
            aliOSSPolicy.setPolicy(encodedPolicy);
            aliOSSPolicy.setSignature(postSignature);
            aliOSSPolicy.setHost(host);
            aliOSSPolicy.setDir(DIR);
        }catch (Exception e){
            log.info("server end generate alioss policy failed.",e);
        }
        return CommonResponse.createForSuccess(aliOSSPolicy);
    }
}
