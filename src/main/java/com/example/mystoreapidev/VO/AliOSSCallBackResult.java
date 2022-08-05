package com.example.mystoreapidev.VO;

import lombok.Data;

@Data
public class AliOSSCallBackResult {
    private String filename;
    private String size;
    private String mimeType;
    private String height;
    private String width;
}
