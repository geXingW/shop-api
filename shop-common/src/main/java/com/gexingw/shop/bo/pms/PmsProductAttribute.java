package com.gexingw.shop.bo.pms;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gexingw.shop.constant.SystemConstant;
import lombok.Data;

import java.util.Date;

@Data
@TableName("pms_product_attribute")
public class PmsProductAttribute {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Long categoryId;

    private String name;

    private Integer inputType;

    private String inputValue;

    private Boolean searchable;

    private Integer sort;

    private Integer type;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = SystemConstant.DATETIME_STRING_FORMAT, timezone = "GMT+8")
    private Date createTime;

}
