package com.gexingw.shop.enums;

import lombok.Data;
import lombok.Getter;

@Getter
public enum BannerShowStatusEnum {
    SHOW(1, "展示"),
    HIDDEN(0, "隐藏");

    final private Integer code;

    final private String message;

    BannerShowStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
