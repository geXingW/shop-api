package com.gexingw.shop.dto.ums;

import lombok.Data;

@Data
public class UmsMemberRecvAddressRequestParam {
    private Long id;

    private Long memberId;

    private String name;

    private Integer phoneNumber;

    private Integer defaultStatus;

    private Integer postCode;

    private String province;

    private String city;

    private String region;

    private String detailAddress;
}
