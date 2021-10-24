package com.gexingw.shop.bo.ums;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ums_member_recv_address")
public class UmsMemberRecvAddress {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long memberId;

    private String name;

    private String phoneNumber;

    private Integer defaultStatus;

    private Integer postCode;

    private String province;

    private String city;

    private String region;

    private String detailAddress;
}
