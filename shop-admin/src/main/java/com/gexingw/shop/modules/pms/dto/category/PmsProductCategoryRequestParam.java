package com.gexingw.shop.modules.pms.dto.category;

import lombok.Data;

@Data
public class PmsProductCategoryRequestParam {
    private Long id;

    private Long pid;

    private String name;

    private Integer sort;

    private Integer showStatus;

    private String keywords;

    private String icon;

}
