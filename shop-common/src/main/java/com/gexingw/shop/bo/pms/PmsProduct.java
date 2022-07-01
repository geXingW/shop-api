package com.gexingw.shop.bo.pms;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gexingw.shop.constant.SystemConstant;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("pms_products")
public class PmsProduct {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String title;

    // 商品主图，上传的第一张图片
    private String pic;

    private String subTitle;

    private Long brandId;

    private Long categoryId;

    @TableField(exist = false)
    private String categoryName;

    private Integer onSale = 0;

    // 销售价
    private BigDecimal salePrice;

    // 市场价
    private BigDecimal originalPrice;

    // 促销价
    private BigDecimal promotionPrice;

    private Integer sort;

    private Integer saleCnt;

    private Integer stock;  // 库存

    private Integer lowStock; // 预警库存

    private String unit; // 单位

    private String keywords;

    private String isNew;  // 新品

    private String isRecommend; // 推荐

    @TableField(value = "detail_pc_html")
    private String detailPCHtml; // PC端详情显示

    private String detailMobileHtml; // 手机端详情显示

    private String albumPics; // 商品相册

    private String skuOptions; // 商品SKU可选项

    @JsonFormat(pattern = SystemConstant.DATETIME_STRING_FORMAT, timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @JsonFormat(pattern = SystemConstant.DATETIME_STRING_FORMAT, timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

//    public String getPic() {
//        SysUploadMapper sysUploadMapper = SpringContextUtil.getBean(SysUploadMapper.class);
//        SysUpload upload = sysUploadMapper.selectOne(new QueryWrapper<SysUpload>().eq("upload_id", id)
//                .eq("upload_module", UploadConstant.UPLOAD_MODULE_PRODUCT));
//
//        return upload != null ? upload.getFullUrl() : "";
//    }

//    public void setAlbumPics(List<String> picsUrl) {
//        List<String> picPaths = picsUrl.stream().map(picUrl -> picUrl.substring(picUrl.indexOf(UploadConstant.UPLOAD_TYPE_IMAGE)))
//                .collect(Collectors.toList());
//
//        this.albumPics = String.join(",", picPaths);
//    }

//    public List<String> getAlbumPics() {
//        return Arrays.stream(this.albumPics.split(",")).collect(Collectors.toList());
//    }
}
