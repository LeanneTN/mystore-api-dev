package com.example.mystoreapidev.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
//SpringBoot uses Jackson to serialize class into Json
//get rid of null on response body
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {
    private Integer code;
    private String message;
    private T data;

    protected CommonResponse(Integer code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess(){
        return this.code == ResponseCode.SUCCESS.getCode();
    }

    public static <T> CommonResponse<T> createForSuccess(){
        return new CommonResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getDescription(), null);
    }

    public static <T> CommonResponse<T> createForSuccess(T data){
        return new CommonResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getDescription(), data);
    }

    public static <T> CommonResponse<T> createForError(){
        return new CommonResponse<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDescription(), null);
    }

    // set error message
    public static <T> CommonResponse<T> createForError(String message){
        return new CommonResponse<>(ResponseCode.ERROR.getCode(), message, null);
    }

    // set error code and message
    public static <T> CommonResponse<T> createForError(Integer code, String message){
        return new CommonResponse<>(code, message, null);
    }
}
