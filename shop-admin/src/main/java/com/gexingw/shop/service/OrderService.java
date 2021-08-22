package com.gexingw.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.oms.OmsOrder;
import com.gexingw.shop.bo.oms.OmsOrderItemDetail;
import com.gexingw.shop.bo.oms.OmsOrderRecvAddress;

import java.util.List;
import java.util.Set;

public interface OrderService {
    boolean deleteByOrderIds(Set<Long> ids);

    IPage<OmsOrder> search(QueryWrapper<OmsOrder> queryWrapper, Page<OmsOrder> page);

    OmsOrder findById(Long id);

    List<OmsOrderItemDetail> getOrderItemDetailsByOrderId(Long orderId);

    OmsOrderRecvAddress getOrderRecvAddressByOrderId(Long orderId);
}
