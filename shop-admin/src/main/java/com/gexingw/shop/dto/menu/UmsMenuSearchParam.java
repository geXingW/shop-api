package com.gexingw.shop.dto.menu;

import lombok.Data;

import java.util.List;

@Data
public class UmsMenuSearchParam {

    private int pid;

    private int page = 1;

    private int size = 10;

    private Long id;

    private String furry;

    private List<String> createTime;
}
