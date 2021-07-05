package com.gexingw.shop.bo.ums;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ums_dict_details")
public class UmsDictDetail {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private Long dictId;

    private String label;

    private String value;

    private int dictSort;

    @TableField(exist = false)
    private UmsDict dict;
}
