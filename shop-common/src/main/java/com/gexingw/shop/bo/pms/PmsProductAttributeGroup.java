package com.gexingw.shop.bo.pms;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gexingw.shop.constant.SystemConstant;
import lombok.Data;

import java.util.Date;

@Data
@TableName("pms_product_attribute_group")
public class PmsProductAttributeGroup {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private Long categoryId;

    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = SystemConstant.DATETIME_STRING_FORMAT, timezone = "GMT+8")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = SystemConstant.DATETIME_STRING_FORMAT, timezone = "GMT+8")
    private Date updateTime;
}
