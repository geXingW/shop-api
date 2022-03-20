package com.gexingw.shop.modules.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gexingw.shop.bo.oms.OmsOrderItemDetail;
import com.gexingw.shop.mapper.oms.OmsOrderItemDetailMapper;
import com.gexingw.shop.modules.oms.service.OmsOrderItemDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class OmsOrderItemDetailServiceImpl extends ServiceImpl<OmsOrderItemDetailMapper, OmsOrderItemDetail> implements OmsOrderItemDetailService {

    @Resource
    OmsOrderItemDetailMapper orderItemDetailMapper;

    @Override
    public List<OmsOrderItemDetail> getListByIds(List<String> orderItemIds) {
        if (orderItemIds.size() == 0) {
            return new ArrayList<>();
        }

        QueryWrapper<OmsOrderItemDetail> queryWrapper = new QueryWrapper<OmsOrderItemDetail>().in("item_id", orderItemIds);
        return orderItemDetailMapper.selectBatchIds(orderItemIds);
    }
}
