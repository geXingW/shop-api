package com.gexingw.shop.modules.pms.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class PmsProductCategoryTreeVO {
    private Long id;

    private String name;

    private String icon;

    private Boolean leaf;

    @JsonInclude
    private List<PmsProductCategoryTreeVO> children;

    public void setChildren(List<PmsProductCategoryTreeVO> children) {
        this.children = children;

        // 列表大于0，代表有子分类
        this.leaf = children.size() <= 0;
    }
}
