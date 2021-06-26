package com.gexingw.shop.dto.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PmsProductRequestParam {

    private Long id;

    private Long brandId;

    private Long categoryId;

    private String name;

    private String pic;

    private Integer status;

    private BigDecimal price;
}
