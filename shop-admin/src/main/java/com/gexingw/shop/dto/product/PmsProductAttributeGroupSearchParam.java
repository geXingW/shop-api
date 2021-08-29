package com.gexingw.shop.dto.product;

import com.gexingw.shop.dto.BaseSearchParam;
import lombok.Data;

import java.util.List;

@Data
public class PmsProductAttributeGroupSearchParam extends BaseSearchParam {
    private Long groupId;

    private Integer type;

    private boolean attached = true;
}
