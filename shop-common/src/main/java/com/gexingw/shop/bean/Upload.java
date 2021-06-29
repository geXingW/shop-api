package com.gexingw.shop.bean;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gexingw.shop.config.FileConfig;
import com.gexingw.shop.utils.SpringContextUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.File;
import java.util.Date;


@Data
@TableName("uploads")
public class Upload {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String path;

    private Long uploadId;

    private String uploadType;

    private Long size;

    private String disk;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public String getFullPath() {
        FileConfig fileConfig = SpringContextUtil.getBean(FileConfig.class);

        String path = StrUtil.removeSuffix(fileConfig.getLocation(this.disk), File.separator) + File.separator;
        return path + StrUtil.removePrefix(this.path, File.separator);
    }

    public String getFullUrl() {
        FileConfig fileConfig = SpringContextUtil.getBean(FileConfig.class);

        String path = StrUtil.removeSuffix(fileConfig.getDiskHost(this.disk), File.separator) + File.separator;
        return path + StrUtil.removePrefix(this.path, File.separator);
    }
}
