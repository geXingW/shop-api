package com.gexingw.shop.modules.oms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.oms.OmsOrder;
import com.gexingw.shop.bo.oms.OmsOrderItemDetail;
import com.gexingw.shop.bo.oms.OmsOrderRecvAddress;
import com.gexingw.shop.modules.oms.dto.OmsOrderRequestParam;

import java.util.List;
import java.util.Set;

public interface OmsOrderService {
    IPage<OmsOrder> search(QueryWrapper<OmsOrder> queryWrapper, IPage<OmsOrder> page);

    OmsOrder getById(String id);

    Long save(OmsOrderRequestParam requestParam);

    boolean update(OmsOrderRequestParam requestParam);

    boolean deleteByIds(Set<Long> ids);

    List<OmsOrderItemDetail> getOrderItemDetailsByOrderId(String orderId);

    OmsOrderRecvAddress getOrderRecvAddressByOrderId(String orderId);
}
