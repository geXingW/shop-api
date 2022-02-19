package com.gexingw.shop.bo.ums;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ums_member_recv_address")
public class UmsMemberRecvAddress {

    public static Integer ADDRESS_DEFAULT = 1;  // 默认地址

    public static Integer ADDRESS_NOT_DEFAULT = 0; // 非默认地址

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long memberId;

    private String name;

    private String phoneNumber;

    private Integer defaultStatus;

    private Integer postCode;

    private String provinceName;

    private Integer provinceCode;

    private String cityName;
    private Integer cityCode;

    private String regionName;
    private Integer regionCode;

    private String detailAddress;
}
