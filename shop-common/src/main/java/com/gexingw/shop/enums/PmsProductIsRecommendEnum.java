package com.gexingw.shop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PmsProductIsRecommendEnum {
    RECOMMEND(1, "推荐"),
    NOT_RECOMMEND(0, "不推荐"),
    ;

    final private Integer code;
    final private String message;
}
