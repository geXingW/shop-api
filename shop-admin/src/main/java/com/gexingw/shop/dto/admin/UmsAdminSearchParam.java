package com.gexingw.shop.dto.admin;

import lombok.Data;

import java.util.List;

@Data
public class UmsAdminSearchParam {

    private String blurry;

    private long deptId = -1L;

    private int enabled = -1;

    private int page = 0;

    private int size = 10;

    private String[] createTime = new String[]{};

}