package com.example.mystoreapidev.common;

import lombok.Getter;

@Getter
public enum ResponseCode {
    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR"),
    ARGUMENT_ILLEGAL(10, "ARGUMENT_ILLEGAL"),
    NEED_LOGIN(11, "NEED LOGIN");

    private final int code;
    private final String description;

    ResponseCode(int code, String description){
        this.code = code;
        this.description = description;
    }
}
