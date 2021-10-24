package com.gexingw.shop.modules.ums.dto.job;

import com.gexingw.shop.dto.BaseSearchParam;
import lombok.Data;

@Data
public class UmsJobSearchParam extends BaseSearchParam {
    private Long id;

    private String name;

    private Boolean enabled;

    private int jobSort;

}
