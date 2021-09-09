package com.gexingw.shop.dto.product;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class PmsProductRequestParam {

    private Long id;

    private String title;

    private String subTitle;

    private Long brandId;

    private Long categoryId;

    private Integer sort;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private Integer stock;  // 库存

    private Integer lowStock; // 预警库存

    private String unit;

    private String detailPCHtml;

    private String detailMobileHtml;

    private List<String> pics = new ArrayList<>();

    private List<SKU> skuList = new ArrayList<>();

    private List<Attribute> attributeList = new ArrayList<>();

    private String onSale;

    private String keywords;

    private String pic = ""; // 图片

    private String isNew;  // 新品

    private String isRecommend; // 推荐

    @Data
    public static class SKU {
        private BigDecimal price;

        private Integer stock;

        private BigDecimal originPrice;

        private List<Attribute> sku = new ArrayList<>();
    }

    @Data
    public static class Attribute {
        private Long id;

        private String name;

        private String value;
    }
}
