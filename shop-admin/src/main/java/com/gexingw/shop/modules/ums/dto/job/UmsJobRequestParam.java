package com.gexingw.shop.modules.ums.dto.job;

import lombok.Data;

import java.util.Date;

@Data
public class UmsJobRequestParam {
    private Long id;

    private String name;

    private Boolean enabled;

    private int jobSort;

    private Date createTime;
}
