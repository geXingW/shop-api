package com.gexingw.shop.dto;

import lombok.Data;

import java.util.Arrays;

@Data
public class BaseSearchParam {
    private int page = 0;

    private int size = 10;

    // 模糊查询字段
    private String blurry;

    private String[] sort;

    private String[] sortRule;

    // 创建时间
    private String[] createTime;

    public int getPage() {
        return page == 0 ? ++page : page;
    }

    public String sortBy() {
        if (sortRule == null) {
            sortRule();
        }
        return sortRule[1];
    }

    public String sortField() {
        if (sortRule == null) {
            sortRule();
        }

        return sortRule[0];
    }

    public boolean sortAsc() {
        if (sortRule == null) {
            sortRule();
        }

        return "asc".equals(sortBy());
    }

    public void sortRule() {
        sortRule = (sort == null || sort.length <= 1) ? new String[]{"id", "asc"} : sort;
    }

    public String getCreateTimeBegin() {
        if (createTime == null || createTime.length < 1) {
            return null;
        }

        return createTime[0];
    }

    public String getCreateTimeEnd() {
        if (createTime == null || createTime.length < 2) {
            return null;
        }

        return createTime[1];
    }
}
