package com.gexingw.shop.dto.product;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class PmsProductCategoryRequestParam {
    private Long id;

    private Long pid;

    private String name;

    private Integer level;

    private Integer sort;

    private Integer showStatus;

    private String keywords;

    private String icon;

}
