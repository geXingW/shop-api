package com.gexingw.shop.bean.ums;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@TableName("ums_depts")
public class UmsDept {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long pid;

    private int subCount;

    private String name;

    private int deptSort;

    private boolean enabled;

    @TableField(exist = false)
    private List<UmsDept> children;

//    @TableField(exist = false)
//    private boolean hasChildren;

    public String getLabel() {
        return name;
    }

    public boolean getLeaf() {
        return subCount == 0;
    }

    public boolean getHasChildren() {
        return subCount != 0;
    }
}