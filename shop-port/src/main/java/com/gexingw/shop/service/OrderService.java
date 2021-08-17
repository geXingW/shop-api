package com.gexingw.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.oms.OmsOrder;
import com.gexingw.shop.bo.oms.OmsOrderItemDetail;
import com.gexingw.shop.bo.oms.OmsOrderRecvAddress;
import com.gexingw.shop.dto.oms.OmsOrderRequestParam;

import java.util.List;
import java.util.Set;

public interface OrderService {
    IPage<OmsOrder> search(QueryWrapper<OmsOrder> queryWrapper, IPage<OmsOrder> page);

    OmsOrder findById(Long id);

    Long save(OmsOrderRequestParam requestParam);

    boolean update(OmsOrderRequestParam requestParam);

    boolean deleteByIds(Set<Long> ids);

    List<OmsOrderItemDetail> getOrderItemDetailsByOrderId(Long orderId);

    OmsOrderRecvAddress getOrderRecvAddressByOrderId(Long orderId);
}
