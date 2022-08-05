package com.example.mystoreapidev.VO;

import lombok.Data;

@Data
public class AliOSSPolicy {
    private String accessId;
    private String policy;
    private String signature;
    private String dir;
    private String host;

    private String callback;
}
