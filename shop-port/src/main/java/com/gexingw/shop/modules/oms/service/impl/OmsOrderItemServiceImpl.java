package com.gexingw.shop.modules.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.oms.OmsOrderItem;
import com.gexingw.shop.mapper.oms.OmsOrderItemMapper;
import com.gexingw.shop.modules.oms.service.OmsOrderItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OmsOrderItemServiceImpl implements OmsOrderItemService {

    @Resource
    OmsOrderItemMapper orderItemMapper;

    @Override
    public List<String> getItemDetailIdsByOrderId(String orderId) {
        QueryWrapper<OmsOrderItem> queryWrapper = new QueryWrapper<OmsOrderItem>().eq("order_id", orderId);
        return orderItemMapper.selectList(queryWrapper).stream().map(OmsOrderItem::getOrderItemDetailId).collect(Collectors.toList());
    }
}
