package com.gexingw.shop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PmsProductIsNewEnum {
    NEW(1, "新品"),
    NOT_NEW(0, "非新品"),
    ;

    final private Integer code;
    final private String message;
}
