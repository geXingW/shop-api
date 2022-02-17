package com.gexingw.shop.bo.oms;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gexingw.shop.constant.SystemConstant;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OmsCartItem {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long memberId;

    private String itemId;

    private BigDecimal itemPrice;

    private Integer itemQuantity;

    private String itemPic;

    private String itemTitle;

    private String itemSubTitle;

    private Long skuId;

    private String skuData;

    private Integer checked;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = SystemConstant.DATETIME_STRING_FORMAT, timezone = "GMT+8")
    private Date createTime;

}
