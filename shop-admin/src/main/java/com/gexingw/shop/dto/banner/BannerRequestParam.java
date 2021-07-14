package com.gexingw.shop.dto.banner;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class BannerRequestParam {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer sort;

    private String pic;

    private String link;

    private String startTime;

    private String endTime;

}
