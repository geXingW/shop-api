package com.gexingw.shop.modules.pms.dto.category;

import com.gexingw.shop.dto.BaseSearchParam;
import lombok.Data;

@Data
public class PmsProductCategorySearchParam extends BaseSearchParam {
    private Long pid;

    private Long id;

    private Long categoryId;
}
