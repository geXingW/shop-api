package com.gexingw.shop.dto.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PmsProductRequestParam {

    private Long id;

    private String title;

    private String subTitle;

    private Long brandId;

    private Long categoryId;

    private String onSale;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private Integer stock;  // 库存

    private Integer lowStock; // 预警库存

    private String unit; // 单位

    private String keywords;

    private String pic = ""; // 图片
}
