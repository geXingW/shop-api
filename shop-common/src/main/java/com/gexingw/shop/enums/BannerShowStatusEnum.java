package com.gexingw.shop.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BannerShowStatusEnum {
    SHOW(1, "展示"),
    HIDDEN(0, "隐藏");

    final private Integer code;

    final private String message;

}
