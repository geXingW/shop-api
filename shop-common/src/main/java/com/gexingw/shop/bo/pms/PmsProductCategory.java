package com.gexingw.shop.bo.pms;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gexingw.shop.constant.SystemConstant;
import lombok.Data;

import java.util.Date;

@Data
@TableName("pms_product_categories")
public class PmsProductCategory {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long pid;

    private String name;

    private Integer subCount;

    private Integer showStatus;

    private Integer sort;

    private String icon;

    private String keywords;

    private Integer productCount;

    @JsonFormat(pattern = SystemConstant.DATETIME_STRING_FORMAT, timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @JsonFormat(pattern = SystemConstant.DATETIME_STRING_FORMAT, timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    public boolean isHasChildren() {
        return subCount > 0;
    }

    public void incrSubCount(int incrCnt) {
        subCount += incrCnt;
    }

    public void decrSubCount(int decrCnt) {
        subCount = Math.max(0, subCount - decrCnt);
    }

    public void incrProductCnt(int incrCnt) {
        productCount += incrCnt;
    }

    public void decrProductCnt(int decrCnt) {
        productCount = Math.max(0, productCount - decrCnt);
    }
}
