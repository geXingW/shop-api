package com.gexingw.shop.bo.oms;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gexingw.shop.constant.SystemConstant;
import lombok.Data;

import java.util.Date;

@Data
public class OmsOrderRecvAddress {
    @TableId(value = "id")
    private String id;

    private String orderId;

    private Long recvAddressId;

    private String name;

    private String phoneNumber;

    private Integer postCode;

    private String provinceName;

    private String cityName;

    private String regionName;

    private String detailAddress;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = SystemConstant.DATETIME_STRING_FORMAT, timezone = "GMT+8")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = SystemConstant.DATETIME_STRING_FORMAT, timezone = "GMT+8")
    private Date updateTime;

    public String getFullDetailAddress() {
        return provinceName + cityName + regionName + detailAddress;
    }
}
