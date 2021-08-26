package com.gexingw.shop.enums;


public enum OmsOrderStatusEnum {
    CREATED(0, "待支付"),
    PAYED(1, "待发货"),
    ;

    private Integer code;

    private String text;


    OmsOrderStatusEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }
}
