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

    public interface  CART{
        int CHECKED = 1;
        int UNCHECKED = 0;
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

    @Getter
    public enum PayType {
        ALIPAY(1, "ALIPAY"),
        WECHAT_PAY(2, "WECHAT_PAY"),
        OTHER(3, "OTHER");


        private final int code;
        private final String description;

        PayType(int code, String description){
            this.code = code;
            this.description = description;
        }
    }
    @Getter
    public enum OrderStatus{
        CANCEL(1, "CANCEL"),
        UNPAID(2, "UNPAID"),
        PAID(3, "PAID"),
        SHIPPED(4, "SHIPPED"),
        SUCCESS(5, "EXCHANGE SUCCESS"),
        CLOSED(6, "ORDER CLOSED");


        private final int code;
        private final String description;

        OrderStatus(int code, String description){
            this.code = code;
            this.description = description;
        }
    }

    public interface AlipayTradeStatus{
        String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_CLOSED = "TRADE_CLOSED";
        String TRADE_SUCCESS = "TRADE_SUCCESS";
        String TRADE_FINISHED = "TRADE_FINISHED";
    }

    public interface AlipayCallbackResponse{
        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }
}
