package com.gexingw.shop.bo.oms;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gexingw.shop.constant.SystemConstant;
import lombok.Data;

import java.util.Date;

@Data
public class OmsCartItem {
    private Long id;

    private Long memberId;

    private Long itemId;

    private Long itemPrice;

    private Integer itemQuantity;

    private String itemPic;

    private String itemTitle;

    private String itemSubTitle;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = SystemConstant.DATETIME_STRING_FORMAT, timezone = "GMT+8")
    private Date createTime;

}
