package com.gexingw.shop.dto.product;

import lombok.Data;

@Data
public class PmsProductAttributeRequestParam {
    private Long id;

    private Long categoryId;

    private String name;

    private Integer inputType;

    private String inputValue;

    private Boolean searchable;

    private Integer sort;

    private Integer type;
}
