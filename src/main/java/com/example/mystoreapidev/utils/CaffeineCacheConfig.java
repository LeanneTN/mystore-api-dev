package com.example.mystoreapidev.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineCacheConfig {
    @Bean
    public Cache<String, String> localCache(){
        return Caffeine.newBuilder()
                .initialCapacity(100).maximumSize(300)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();
    }
}
