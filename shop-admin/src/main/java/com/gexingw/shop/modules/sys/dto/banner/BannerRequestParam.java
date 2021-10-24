package com.gexingw.shop.modules.sys.dto.banner;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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

    private String showStatus;

    private Date startTime;

    private Date endTime;

    public void setStartTime(String dateTimeStr){
        startTime = DateUtil.parse(dateTimeStr);
    }

    public void setEndTime(String dateTimeStr){
        endTime = DateUtil.parse(dateTimeStr);
    }

}
