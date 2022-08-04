package com.example.mystoreapidev.utils;

import com.aliyun.oss.OSSClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AliOSSConfig {

    @Value("${aliyun.oss.endpoint}")
    private String ENDPOINT;
    @Value("${aliyun.oss.accessId}")
    private String ACCESS_ID;
    @Value("${aliyun.oss.accessKey}")
    private String ACCESS_KEY;

    @Bean
    public OSSClient ossClient(){
        return new OSSClient(ENDPOINT, ACCESS_ID, ACCESS_KEY);
    }
}
