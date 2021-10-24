package com.gexingw.shop.modules.ums.dto;

import com.gexingw.shop.mapper.sys.SysCityMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
public class UmsMemberRecvAddressRequestParam {

    @Autowired
    SysCityMapper cityMapper;

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

    private List<String> cityIds;
}
