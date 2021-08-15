package com.gexingw.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.oms.OmsOrder;
import com.gexingw.shop.bo.oms.OmsOrderItem;
import com.gexingw.shop.bo.oms.OmsOrderItemDetail;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.dto.oms.OmsOrderRequestParam;
import com.gexingw.shop.exception.DBOperationException;
import com.gexingw.shop.mapper.oms.OmsOrderItemDetailMapper;
import com.gexingw.shop.mapper.oms.OmsOrderItemMapper;
import com.gexingw.shop.mapper.oms.OmsOrderMapper;
import com.gexingw.shop.service.OrderService;
import com.gexingw.shop.service.PmsProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OmsOrderMapper orderMapper;

    @Autowired
    OmsOrderItemDetailMapper itemDetailMapper;

    @Autowired
    OmsOrderItemMapper itemMapper;

    @Autowired
    PmsProductService productService;

    @Override
    public IPage<OmsOrder> search(QueryWrapper<OmsOrder> queryWrapper, IPage<OmsOrder> page) {
        return orderMapper.selectPage(page, queryWrapper);
    }

    @Override
    public OmsOrder findById(Long id) {
        return orderMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(OmsOrderRequestParam requestParam) {
        // 订单
        OmsOrder order = new OmsOrder();
        BeanUtils.copyProperties(requestParam, order);

        if (orderMapper.insert(order) <= 0) {
            throw new DBOperationException("订单信息保存失败！");
        }

        // 订单Item信息
        List<Long> itemDetailIds = saveOrderItemDetails(requestParam.getOrderItems());

        // 进行订单id和订单商品id进行绑定
        for (Long itemDetailId : itemDetailIds) {
            OmsOrderItem orderItem = new OmsOrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setOrderItemDetailId(itemDetailId);

            if (itemMapper.insert(orderItem) <= 0) {
                throw new DBOperationException("订单商品关联失败！");
            }
        }

        return order.getId();
    }

    private List<Long> saveOrderItemDetails(List<OmsOrderRequestParam.OmsOrderItemRequestParam> orderItems) {
        ArrayList<Long> itemDetailIds = new ArrayList<>();

        // 根据itemId，查询所有商品详情
        List<Long> itemIds = orderItems.stream().map(OmsOrderRequestParam.OmsOrderItemRequestParam::getItemId).collect(Collectors.toList());

        // 将商品列表按照id，组成Map,方便后续取值
        HashMap<Long, PmsProduct> productsMap = new HashMap<>();
        List<PmsProduct> products = productService.getByIds(itemIds);
        for (PmsProduct product : products) {
            productsMap.put(product.getId(), product);
        }

        for (OmsOrderRequestParam.OmsOrderItemRequestParam itemInfo : orderItems) {
            PmsProduct product = productsMap.get(itemInfo.getItemId());
            if (product == null) {
                continue;
            }

            OmsOrderItemDetail itemDetail = new OmsOrderItemDetail();
            itemDetail.setItemId(product.getId());
            itemDetail.setItemName(product.getTitle());
            itemDetail.setItemPrice(product.getPrice());
            itemDetail.setItemQuantity(itemInfo.getItemQuantity());
            if (itemDetailMapper.insert(itemDetail) <= 0) {
                throw new DBOperationException("订单商品详情保存失败！");
            }

            itemDetailIds.add(itemDetail.getId());
        }

        return itemDetailIds;
    }

    @Override
    public boolean update(OmsOrderRequestParam requestParam) {
        OmsOrder order = findById(requestParam.getId());
        if (order == null) {
            return false;
        }

        BeanUtils.copyProperties(requestParam, order);

        return orderMapper.updateById(order) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByIds(Set<Long> ids) {
        // fixme 订单删除之后，订单商品信息是否应该删除

        // 查找订单商品ids
        List<Long> itemDetailIds = itemMapper.selectList(new QueryWrapper<OmsOrderItem>().in("order_id", ids))
                .stream().map(OmsOrderItem::getOrderItemDetailId).collect(Collectors.toList());

        // 删除订单商品信息信息
        if (itemDetailMapper.deleteBatchIds(itemDetailIds) <= 0) {
            throw new DBOperationException("订单商品信息删除失败！");
        }

        // 删除订单商品关联
        if (itemMapper.delete(new QueryWrapper<OmsOrderItem>().in("order_id", ids)) <= 0) {
            throw new DBOperationException("订单商品关联删除失败！");
        }

        // 删除订单信息
        if (orderMapper.deleteBatchIds(ids) <= 0) {
            throw new DBOperationException("订单信息删除失败！");
        }

        return true;
    }
}
