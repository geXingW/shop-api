package com.gexingw.shop.bean.ums;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.context.annotation.Bean;

@Data
@TableName("ums_dicts")
public class UmsDict {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String name;

    private String description;

}
