package com.gexingw.shop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PmsProductOnSaleEnum {
    ON_SALE(1, "上架"),
    OFF_SALE(0, "下架"),
    ;

    final private Integer code;

    final private String message;
}
