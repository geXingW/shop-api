package com.gexingw.shop.modules.pms.vo.product;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.config.FileConfig;
import com.gexingw.shop.modules.pms.dto.product.PmsProductRequestParam;
import com.gexingw.shop.utils.FileUtil;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

@Data
public class PmsProductInfoVO {
    private String id;

    private String title;

    // 商品主图，上传的第一张图片
    private String pic;

    private List<String> albumPics = new ArrayList<>(); // 商品相册

    private String subTitle;

    private Long categoryId;

    private String categoryName;

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

    private String onSale;

    private String isNew;

    private List<PmsProductRequestParam.SkuOption> skuOptions = new ArrayList<>();

    public PmsProductInfoVO setProductInfo(PmsProduct product) {
        BeanUtils.copyProperties(product, this);
        this.setOnSale(product.getOnSale().toString());
        return this;
    }

    public PmsProductInfoVO setProductPics(PmsProduct product, FileConfig fileConfig) {
        String fileDomain = fileConfig.getDiskHost();

        // 主图
        this.pic = FileUtil.buildFileFullUrl(fileDomain, product.getPic());

        // 相册
        String[] picPaths = product.getAlbumPics().split(",");
        for (String picPath : picPaths) {
            this.albumPics.add(FileUtil.buildFileFullUrl(fileDomain, picPath));
        }

        // 商品详情
        String separator = Matcher.quoteReplacement(File.separator);
        this.detailPCHtml = product.getDetailPCHtml().replaceAll("src=\"", "src=\"" + fileDomain + separator);
        this.detailMobileHtml = product.getDetailMobileHtml().replaceAll("src=\"", "src=\"" + fileDomain + separator);

        // 商品Sku选项
        TypeReference<List<PmsProductRequestParam.SkuOption>> jsonTypeReference = new TypeReference<List<PmsProductRequestParam.SkuOption>>() {
        };
        this.skuOptions = JSON.parseObject(product.getSkuOptions(), jsonTypeReference);

        return this;
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


