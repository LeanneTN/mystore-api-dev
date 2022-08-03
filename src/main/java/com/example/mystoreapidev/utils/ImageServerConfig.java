package com.example.mystoreapidev.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties("image.server")
@Data
@Component
public class ImageServerConfig {
    private String url;
    private String username;
    private String password;
}
