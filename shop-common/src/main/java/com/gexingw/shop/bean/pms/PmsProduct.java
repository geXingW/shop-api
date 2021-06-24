package com.gexingw.shop.bean.pms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("pms_products")
public class PmsProduct {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long brandId;

    private Long categoryId;

    private String name;

    private Date createTime;

    private Date updateTime;
}
