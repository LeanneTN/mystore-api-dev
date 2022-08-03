package com.example.mystoreapidev.common;

import lombok.Getter;

public class CONSTANT {
    public static final String LOGIN_USER = "loginUser";

    public static final Integer CATEGORY_ROOT = 0;

    public static final String PRODUCT_BY_ASC = "price_asc";
    public static final String PRODUCT_BY_DESC = "price_desc";

    public interface ROLE{
        int CUSTOMER = 1;
        int ADMIN = 0;
    }

    @Getter
    public enum ProductStatus{
        ON_SALE(1, "on_sale"),
        TAKE_DOWN(2, "take_down"),
        DELETE(3, "delete");


        private final int code;
        private final String description;

        ProductStatus(int code, String description){
            this.code = code;
            this.description = description;
        }
    }
}
