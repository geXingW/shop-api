package com.gexingw.shop.enums;

public enum OmsOrderPayType {
    WAIT_PAY(0, "待支付"),
    ALIPAY(1, "支付宝"),
    WX_PAY(2, "微信"),
    ;

    private Integer code;
    private String text;

    OmsOrderPayType(Integer code, String text) {
        this.code = code;
        this.text = text;
    }

    public Integer getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
