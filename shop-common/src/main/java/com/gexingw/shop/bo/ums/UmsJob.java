package com.gexingw.shop.bo.ums;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("ums_jobs")
public class UmsJob {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String name;

    private boolean enabled;

    private int jobSort;

    private Date createTime;
}
