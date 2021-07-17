package com.gexingw.shop.bo.ums;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gexingw.shop.constant.SystemConstant;
import lombok.Data;

import java.util.Date;

@Data
@TableName("ums_menus")
public class UmsMenu {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long pid;

    private int subCount;

    private int type;

    private String title;

    private String name;

    private String component;

    private int menuSort;

    private String icon;

    private String path;

    @TableField("i_frame")
    private boolean iframe;

    @TableField("`cache`")
    private boolean cache;

    private boolean hidden;

    private String permission;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = SystemConstant.DATETIME_STRING_FORMAT, timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = SystemConstant.DATETIME_STRING_FORMAT, timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    public boolean isHasChildren() {
        return subCount > 0;
    }

    public String getLabel() {
        return title;
    }

    public String getComponentName() {
        return name;
    }

    public boolean isLeaf() {
        return !isHasChildren();
    }

    public void incrSubCount(int incrCnt) {
        this.subCount += incrCnt;
    }

    public void decrSubCount(int decrCnt) {
        this.subCount -= decrCnt;
    }
}

