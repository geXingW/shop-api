package com.gexingw.shop.modules.ums.dto.menu;

import lombok.Data;

@Data
public class UmsMenuRequestParam {
    private Long id;

    private Long pid;

    private int subCount;

    private int type;

    private String title;

    private String name;

    private String component;

    private int menuSort;

    private String icon;

    private String path;

    private boolean iFrame;

    private boolean cache;

    private boolean hidden;

    private String permission;
}
