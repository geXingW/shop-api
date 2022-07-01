package com.gexingw.shop.modules.pms.dto.product;

import com.gexingw.shop.config.FileConfig;
import com.gexingw.shop.utils.StringUtil;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PmsProductRequestParam {

    private Long id;

    @Size(min = 5, max = 50)
    private String title;

    @Size(min = 5, max = 100)
    private String subTitle;

    private Long brandId;

    @Min(1)
    @Max(10000000L)
    private Long categoryId;

    @Min(1)
    @Max(100000L)
    private Integer sort;

    @DecimalMin("0.01")
    @DecimalMax("10000000")
    private BigDecimal salePrice;

    @DecimalMin("0.01")
    @DecimalMax("10000000")
    private BigDecimal originalPrice;

    @Range(min = 1L, max = 10000000L)
    private Integer stock;  // 库存

    private Integer lowStock; // 预警库存

    @Size(min = 1, max = 5)
    private String unit;

    private String detailPCHtml;

    private String detailMobileHtml;

    @Size(min = 1, max = 5)
    private List<String> pics = new ArrayList<>();

    private List<Sku> skuList = new ArrayList<>();

    private List<SkuOption> skuOptions = new ArrayList<>();

    private List<Attribute> attributeList = new ArrayList<>();

    private Integer onSale;

    private String keywords;

    private String pic = ""; // 图片

    private String isNew;  // 新品

    private String isRecommend; // 推荐

    @Data
    public static class Sku {
        private BigDecimal price;

        private Integer stock;

        private Integer lowStock;

        private BigDecimal originPrice;

        private List<Attribute> sku = new ArrayList<>();
    }

    @Data
    public static class Attribute {
        private Long id;

        private String name;

        private String value;
    }

    @Data
    public static class SkuOption {
        private Long attributeId;

        private String attributeName;

        private List<String> attributeValues;
    }

    public List<String> getPicsWithoutDomain(FileConfig config) {
        String domain = config.getDiskHost();
        return this.pics.stream().map(pic -> StringUtil.trim(pic.replace(domain, ""), File.separator))
                .collect(Collectors.toList());
    }
}
