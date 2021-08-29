package com.gexingw.shop.dto.product;

import lombok.Data;

import java.util.List;

@Data
public class PmsProductAttributeGroupRequestParam {
    private Long id;

    private String name;

    private Long categoryId;

    private Integer sort;

    private Long groupId;

    private List<Long> attributeIds;
}
