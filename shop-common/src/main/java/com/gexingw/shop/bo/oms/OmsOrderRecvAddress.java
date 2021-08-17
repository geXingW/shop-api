package com.gexingw.shop.bo.oms;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gexingw.shop.constant.SystemConstant;
import lombok.Data;

import java.util.Date;

@Data
public class OmsOrderRecvAddress {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private Long recvAddressId;

    private String recvName;

    private String recvPhone;

    private Integer recvPostCode;

    private String recvProvince;

    private String recvCity;

    private String recvRegion;

    private String recvDetailAddress;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = SystemConstant.DATETIME_STRING_FORMAT, timezone = "GMT+8")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = SystemConstant.DATETIME_STRING_FORMAT, timezone = "GMT+8")
    private Date updateTime;

}
