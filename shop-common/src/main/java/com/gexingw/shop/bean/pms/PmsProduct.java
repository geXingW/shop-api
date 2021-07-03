package com.gexingw.shop.bean.pms;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gexingw.shop.bean.Upload;
import com.gexingw.shop.constant.UploadConstant;
import com.gexingw.shop.mapper.UploadMapper;
import com.gexingw.shop.utils.SpringContextUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("pms_products")
public class PmsProduct {
    @TableId(value = "id", type = IdType.AUTO)
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    public String getPic() {
        UploadMapper uploadMapper = SpringContextUtil.getBean(UploadMapper.class);
        Upload upload = uploadMapper.selectOne(new QueryWrapper<Upload>().eq("upload_id", id)
                .eq("upload_type", UploadConstant.UPLOAD_TYPE_PRODUCT));

        return upload != null ? upload.getFullUrl() : null;
    }
}
