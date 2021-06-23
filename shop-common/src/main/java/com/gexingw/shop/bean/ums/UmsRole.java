package com.gexingw.shop.bean.ums;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("ums_roles")
public class UmsRole {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String name;

    private int level;

    private String description;

    private String dataScope;

    private Long createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(fill= FieldFill.INSERT)
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(fill= FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private List<UmsMenu> menus;

    @TableField(exist = false)
    private List<UmsDept> depts;
}