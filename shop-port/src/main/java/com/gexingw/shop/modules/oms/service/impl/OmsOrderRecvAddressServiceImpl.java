package com.gexingw.shop.modules.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.oms.OmsOrderRecvAddress;
import com.gexingw.shop.mapper.oms.OmsOrderRecvAddressMapper;
import com.gexingw.shop.modules.oms.service.OmsOrderRecvAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OmsOrderRecvAddressServiceImpl implements OmsOrderRecvAddressService {
    @Resource
    OmsOrderRecvAddressMapper orderRecvAddressMapper;

    @Override
    public OmsOrderRecvAddress findByOrderId(String orderId) {
        return orderRecvAddressMapper.selectOne(new QueryWrapper<OmsOrderRecvAddress>().eq("order_id", orderId));
    }
}
