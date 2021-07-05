package com.gexingw.shop.bo.ums;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ums_dicts")
public class UmsDict {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String name;

    private String description;

}
