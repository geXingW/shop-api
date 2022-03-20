package com.gexingw.shop.modules.oms.service;

import com.gexingw.shop.bo.oms.OmsOrderRecvAddress;

public interface OmsOrderRecvAddressService {
    OmsOrderRecvAddress findByOrderId(String id);
}
