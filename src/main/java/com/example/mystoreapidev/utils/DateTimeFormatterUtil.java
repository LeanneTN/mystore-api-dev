package com.example.mystoreapidev.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatterUtil {
    private static final String DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    private static final String DATETIME_PATTERN_GMT = "yyyy-MM-dd HH:mm:ss";

    private DateTimeFormatterUtil(){};

    public static String format(LocalDateTime dateTime){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        if(dateTime == null){
            return "";
        }
        return dateTimeFormatter.format(dateTime);
    }

    public static LocalDateTime parseGMT(String gmtString){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_PATTERN_GMT);
        if(gmtString!= null){
            return LocalDateTime.from(dateTimeFormatter.parse(gmtString));
        }
        return null;
    }
}
