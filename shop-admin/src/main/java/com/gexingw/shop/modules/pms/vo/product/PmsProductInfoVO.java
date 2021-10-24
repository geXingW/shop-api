package com.gexingw.shop.modules.pms.vo.product;

import com.gexingw.shop.bo.pms.PmsProduct;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class PmsProductInfoVO {
    private String id;

    private String title;

    private String subTitle;

    private Long categoryId;

    private Integer sort;

    private Integer saleCnt;

    private BigDecimal salePrice;

    private BigDecimal originalPrice;

    private BigDecimal promotionPrice;

    private Integer stock;

    private Integer lowStock;

    private String unit;

    private String detailPCHtml;

    private String detailMobileHtml;

    private List<SkuListItem> skuList = new ArrayList<>();

    private List<AttributeItem> attributeList = new ArrayList<>();

    private List<String> pics = new ArrayList<>();

    private Integer onSale;

    private Integer isNew;

    public void setProductInfo(PmsProduct product) {
        BeanUtils.copyProperties(product, this);
    }

    @Data
    public static class SkuListItem {
        private BigDecimal price;

        private Integer stock;

        private Integer lowStock;

        private BigDecimal originPrice;

        private List<SkuItem> sku;
    }

    @Data
    public static class SkuItem {
        private Long id;

        private String name;

        private String value;
    }

    @Data
    public static class AttributeItem {
        private Long id;

        private String name;

        private String value;
    }
}


