package com.gexingw.shop.modules.pms.dto.attribute;

import com.gexingw.shop.dto.BaseSearchParam;
import lombok.Data;

@Data
public class PmsProductAttributeGroupSearchParam extends BaseSearchParam {
    private Long groupId;

    private Integer type;

    private boolean attached = true;
}
