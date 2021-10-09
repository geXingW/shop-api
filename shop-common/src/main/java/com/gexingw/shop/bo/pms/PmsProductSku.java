package com.gexingw.shop.bo.pms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("pms_product_sku")
public class PmsProductSku {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String productId;

    private BigDecimal price;

    private Integer stock = 0;

    private Integer lowStock = 0;

    private String spData;
}
