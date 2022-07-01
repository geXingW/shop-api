package com.gexingw.shop.modules.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.oms.OmsOrder;
import com.gexingw.shop.bo.oms.OmsOrderItem;
import com.gexingw.shop.bo.oms.OmsOrderItemDetail;
import com.gexingw.shop.bo.oms.OmsOrderRecvAddress;
import com.gexingw.shop.mapper.oms.OmsOrderItemDetailMapper;
import com.gexingw.shop.mapper.oms.OmsOrderItemMapper;
import com.gexingw.shop.mapper.oms.OmsOrderMapper;
import com.gexingw.shop.mapper.oms.OmsOrderRecvAddressMapper;
import com.gexingw.shop.modules.oms.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OmsOrderMapper orderMapper;

    @Autowired
    OmsOrderItemMapper orderItemMapper;

    @Autowired
    OmsOrderItemDetailMapper orderItemDetailMapper;

    @Autowired
    OmsOrderRecvAddressMapper orderRecvAddressMapper;

    @Override
    public boolean deleteByOrderIds(Set<Long> ids) {
        return orderMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public IPage<OmsOrder> search(QueryWrapper<OmsOrder> queryWrapper, Page<OmsOrder> page) {
        return orderMapper.selectPage(page, queryWrapper);
    }

    @Override
    public OmsOrder findById(String id) {
        return orderMapper.selectById(id);
    }

    @Override
    public List<OmsOrderItemDetail> getOrderItemDetailsByOrderId(String orderId) {
        List<OmsOrderItem> orderItems = orderItemMapper.selectList(new QueryWrapper<OmsOrderItem>().eq("order_id", orderId));

        List<String> itemDetailIds = orderItems.stream().map(OmsOrderItem::getOrderItemDetailId).collect(Collectors.toList());
        if (itemDetailIds.size() == 0) {
            return new ArrayList<>();
        }

        return orderItemDetailMapper.selectList(new QueryWrapper<OmsOrderItemDetail>().in("id", itemDetailIds));
    }

    @Override
    public OmsOrderRecvAddress getOrderRecvAddressByOrderId(String orderId) {
        OmsOrderRecvAddress recvAddress = orderRecvAddressMapper.selectOne(new QueryWrapper<OmsOrderRecvAddress>().eq("order_id", orderId));

        return recvAddress == null ? new OmsOrderRecvAddress() : recvAddress;
    }
}
