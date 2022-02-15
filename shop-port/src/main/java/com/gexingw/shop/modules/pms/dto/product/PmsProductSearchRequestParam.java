package com.gexingw.shop.modules.pms.dto.product;

import com.gexingw.shop.dto.BaseSearchParam;
import lombok.Data;

import java.util.Objects;

@Data
public class PmsProductSearchRequestParam extends BaseSearchParam {
    private String keywords;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PmsProductSearchRequestParam that = (PmsProductSearchRequestParam) o;
        return Objects.equals(keywords, that.keywords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), keywords);
    }
}


