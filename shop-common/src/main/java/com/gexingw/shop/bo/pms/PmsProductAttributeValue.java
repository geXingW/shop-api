package com.gexingw.shop.bo.pms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName
public class PmsProductAttributeValue {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long productId;

    private Long productAttributeId;

    private String productAttributeValue;
}
