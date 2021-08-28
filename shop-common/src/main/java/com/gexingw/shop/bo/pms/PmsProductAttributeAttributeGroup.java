package com.gexingw.shop.bo.pms;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("pms_product_attribute_attribute_group")
public class PmsProductAttributeAttributeGroup {
    private Long product_attribute_id;

    private Long product_attribute_group_id;
}
