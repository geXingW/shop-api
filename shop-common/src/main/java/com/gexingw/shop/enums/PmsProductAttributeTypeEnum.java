package com.gexingw.shop.enums;

public enum PmsProductAttributeTypeEnum {
    BASE_ATTRIBUTE(0, "基本属性"),
    SALE_ATTRIBUTE(1, "销售属性"),
    ;

    private Integer code;

    private String text;

    PmsProductAttributeTypeEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getText() {
        return text;
    }
}
