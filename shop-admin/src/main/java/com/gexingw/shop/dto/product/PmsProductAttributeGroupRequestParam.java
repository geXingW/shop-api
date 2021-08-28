package com.gexingw.shop.dto.product;

import lombok.Data;

@Data
public class PmsProductAttributeGroupRequestParam {
    private Long id;

    private String name;

    private Long categoryId;

    private Integer sort;
}
