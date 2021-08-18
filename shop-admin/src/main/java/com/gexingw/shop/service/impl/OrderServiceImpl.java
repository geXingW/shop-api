package com.gexingw.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.oms.OmsOrder;
import com.gexingw.shop.mapper.oms.OmsOrderMapper;
import com.gexingw.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OmsOrderMapper orderMapper;

    @Override
    public boolean deleteByOrderIds(Set<Long> ids) {
        return orderMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public IPage<OmsOrder> search(QueryWrapper<OmsOrder> queryWrapper, Page<OmsOrder> page) {
        return orderMapper.selectPage(page, queryWrapper);
    }

    @Override
    public OmsOrder findById(Long id) {
        return orderMapper.selectById(id);
    }
}
