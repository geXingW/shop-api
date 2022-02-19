package com.gexingw.shop.modules.ums.dto;

import lombok.Data;

@Data
public class UmsMemberRecvAddressRequestParam {

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
