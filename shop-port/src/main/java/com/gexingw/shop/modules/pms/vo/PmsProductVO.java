package com.gexingw.shop.modules.pms.vo;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.config.FileConfig;
import com.gexingw.shop.utils.FileUtil;
import lombok.Data;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class PmsProductVO {
    private String id;

    private String title;

    private String subTitle;

    private String pic;

    private List<String> albumPics;

    // 销售价
    private BigDecimal salePrice;

    // 市场价
    private BigDecimal originalPrice;

    // 促销价
    private BigDecimal promotionPrice;

    private int saleCnt = 0;

    @TableField(value = "detail_pc_html")
    private String detailPCHtml; // PC端详情显示

    private String detailMobileHtml; // 手机端详情显示

    private String categoryName;

    public PmsProductVO() {
    }

    public PmsProductVO(PmsProduct product) {
        this.setProductInfo(product);
    }

    public PmsProductVO setProductInfo(PmsProduct product) {
        BeanUtil.copyProperties(product, this);

        return this;
    }

    public PmsProductVO setProductPics(PmsProduct product, FileConfig fileConfig) {
        String domain = fileConfig.getDiskHost();

        // 主图
        this.pic = FileUtil.buildFileFullUrl(domain, product.getPic());

        // 相册
        String[] pics = product.getAlbumPics().split(",");
        this.albumPics = new ArrayList<>();
        for (String s : pics) {
            this.albumPics.add(FileUtil.buildFileFullUrl(domain, s));
        }

        // 商品详情图
        this.detailPCHtml = product.getDetailPCHtml().replace("src=\"", "src=\"" + domain + File.separator);

        return this;
    }
}
