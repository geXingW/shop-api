package com.gexingw.shop.modules.pms.dto.product;

import com.gexingw.shop.dto.BaseSearchParam;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PmsProductSearchParam extends BaseSearchParam {
    private Long id;

    private String title;

    private String subTitle; // 子标题

    private Long categoryId;

    private String onSale;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private Integer stock;  // 库存

    private Integer lowStock; // 预警库存

    private String unit; // 单位

    private String keywords;

    private Integer isNew;  // 新品推荐

    private Integer isRecommend;    // 商品推荐

}
