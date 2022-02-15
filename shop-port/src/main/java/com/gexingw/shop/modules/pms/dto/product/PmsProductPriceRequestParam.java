package com.gexingw.shop.modules.pms.dto.product;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PmsProductPriceRequestParam {

    private List<Option> options = new ArrayList<>();

    @Data
    public static class Option {
        private int id;

        private String name;

        private String value;
    }
}
