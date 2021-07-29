package com.gexingw.shop.bo.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class SysCity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer code;

    private Integer parentCode;

}
