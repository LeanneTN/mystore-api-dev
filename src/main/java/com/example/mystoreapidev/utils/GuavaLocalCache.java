package com.example.mystoreapidev.utils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaLocalCache {

    private static LoadingCache<String, String> localCache =
            CacheBuilder.newBuilder().initialCapacity(100).maximumSize(300)
                    .expireAfterWrite(10, TimeUnit.MINUTES).build(
                            new CacheLoader<String, String>() {
                                @Override
                                public String load(String s) throws Exception {
                                    return null;
                                }
                            }
                    );

    public static void setToken(String key, String value){
        localCache.put(key, value);
    }

    public static String getToken(String key) throws Exception{
          return localCache.get(key);
    }
}
