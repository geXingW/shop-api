package com.gexingw.shop.bean;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.context.annotation.Bean;

import java.util.Date;


@Data
@TableName("uploads")
public class Upload {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private Long uploadId;

    private String uploadType;

    private Integer size;

    private String disk;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadTime;
}
