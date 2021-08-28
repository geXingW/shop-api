package com.gexingw.shop.vo.pms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gexingw.shop.constant.SystemConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PmsProductAttributeGroupVO {
    private Long id;

    private String name;

    private Long categoryId;

    private String categoryName;

    private Integer sort;

    @JsonFormat(pattern = SystemConstant.DATETIME_STRING_FORMAT, timezone = "GMT+8")
    private Date createTime;

}
