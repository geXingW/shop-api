package com.gexingw.shop.modules.ums.dto.dept;

import lombok.Data;

@Data
public class UmsDeptRequestParam {

    private long id;

    private long pid = 0;

    private String name;

    private String sort = "id,desc";

    private int page = 0;

    private int size = 10;

    private Boolean enabled;

}

