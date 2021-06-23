package com.gexingw.shop.dto.role;

import lombok.Data;

@Data
public class UmsRoleSearchParam {
    private int page = 0;

    private int size = 10;

    private Long id;

    private String blurry;

    private String[] createTime = new String[]{};

    private String sort;

    public String getSortColumn() {
        String[] split = sort.split(",");
        return split[0];
    }

    public Boolean isSortAsc() {
        String[] split = sort.split(",");
        return "asc".equals(split[1]);
    }
}
