package com.example.mystoreapidev.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtil {
    private BigDecimalUtil(){};

    public static BigDecimal add(double d1, double d2){
        return new BigDecimal(Double.toString(d1)).add(new BigDecimal(Double.toString(d2)));
    }

    public static BigDecimal subtract(double d1, double d2){
        return new BigDecimal(Double.toString(d1)).subtract(new BigDecimal(Double.toString(d2)));
    }

    public static BigDecimal multiply(double d1, double d2){
        return new BigDecimal(Double.toString(d1)).multiply(new BigDecimal(Double.toString(d2)));
    }

    public static BigDecimal divide(double d1, double d2){
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.divide(b2, 2, RoundingMode.UP); //4 and 5 rules
    }
}
