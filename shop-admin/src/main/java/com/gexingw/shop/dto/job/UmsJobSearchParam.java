package com.gexingw.shop.dto.job;

import com.gexingw.shop.dto.BaseSearchParam;
import lombok.Data;

import java.util.Date;

@Data
public class UmsJobSearchParam extends BaseSearchParam {
    private Long id;

    private String name;

    private Boolean enabled;

    private int jobSort;

    private Date createTime;
}
