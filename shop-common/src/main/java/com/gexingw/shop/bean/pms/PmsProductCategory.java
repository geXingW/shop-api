package com.gexingw.shop.bean.pms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class PmsProductCategory {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long pid;

    private String name;

    private Integer level;

    private Integer navStatus;

    private Integer showStatus;

    private Integer sort;

    private String icon;

    private String keywords;

}
