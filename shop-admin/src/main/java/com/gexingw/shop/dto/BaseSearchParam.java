package com.gexingw.shop.dto;

import lombok.Data;

@Data
public class BaseSearchParam {
    private int page = 0;

    private int size = 10;

    private String[] sort;
}
