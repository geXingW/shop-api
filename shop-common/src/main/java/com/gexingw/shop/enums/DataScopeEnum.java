package com.gexingw.shop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataScopeEnum {

    ALL("全部"),
    DEPT("本级"),
    CUSTOMIZE("自定义"),
    ;

    private final String value;

}
