package com.gexingw.shop.modules.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.oms.OmsOrder;
import com.gexingw.shop.bo.oms.OmsOrderItem;
import com.gexingw.shop.bo.oms.OmsOrderItemDetail;
import com.gexingw.shop.bo.oms.OmsOrderRecvAddress;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.modules.oms.dto.OmsOrderRequestParam;
import com.gexingw.shop.exception.DBOperationException;
import com.gexingw.shop.mapper.oms.OmsOrderItemDetailMapper;
import com.gexingw.shop.mapper.oms.OmsOrderItemMapper;
import com.gexingw.shop.mapper.oms.OmsOrderMapper;
import com.gexingw.shop.mapper.oms.OmsOrderRecvAddressMapper;
import com.gexingw.shop.modules.oms.service.CartService;
import com.gexingw.shop.modules.oms.service.OrderService;
import com.gexingw.shop.modules.pms.service.PmsProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
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

    @Autowired
    OmsOrderRecvAddressMapper addressMapper;

    @Autowired
    CartService cartService;

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

        // 用户端传来的商品信息
        List<OmsOrderRequestParam.OrderItems> reqOrderItems = requestParam.getOrderItems();

        // 扣除商品库存
        for (OmsOrderRequestParam.OrderItems reqOrderItem : reqOrderItems) {
            if (!productService.decrProductStockById(reqOrderItem.getItemId(), reqOrderItem.getItemQuantity())) {
                throw new DBOperationException("商品库存扣除失败！");
            }
        }

        // 根据itemId，查询所有商品详情
        List<Long> itemIds = reqOrderItems.stream().map(OmsOrderRequestParam.OrderItems::getItemId).collect(Collectors.toList());

        // 将商品列表按照id，组成Map,方便后续取值
        Map<Long, PmsProduct> orderProductDetailMap = new HashMap<>();
        List<PmsProduct> products = productService.getByIds(itemIds);
        for (PmsProduct product : products) {
            orderProductDetailMap.put(Long.valueOf(product.getId()), product);
        }

        // 订单商品金额
        order.setItemAmount(calcOrderItemTotalAmount(reqOrderItems, orderProductDetailMap));

        // 计算订单总运费
        order.setFreightAmount(calcOrderFreightAmount(reqOrderItems, orderProductDetailMap));

        // 计算订单金额 = 订单商品总金额 + 订单运费总金额
        order.setTotalAmount(order.getItemAmount().add(order.getFreightAmount()));

        // 保存订单信息
        if (orderMapper.insert(order) <= 0) {
            throw new DBOperationException("订单信息保存失败！");
        }

        // 订单Item信息
        List<Long> itemDetailIds = saveOrderItemDetails(reqOrderItems, orderProductDetailMap);

        // 进行订单id和订单商品id进行绑定
        saveOrderItem(Long.valueOf(order.getId()), itemDetailIds);

        // 保存订单收货地址信息
        saveOrderRecvAddress(Long.valueOf(order.getId()), requestParam.getRecvAddress());

        // 移除购物车中的商品
        cartService.delCartItemByItemIds(itemIds);

        return Long.valueOf(order.getId());
    }

    /**
     * 保存订单收货地址信息
     *
     * @param orderId
     * @param recvAddress
     * @return
     */
    private boolean saveOrderRecvAddress(Long orderId, OmsOrderRequestParam.RecvAddress recvAddress) {
        OmsOrderRecvAddress orderRecvAddress = new OmsOrderRecvAddress();
        BeanUtils.copyProperties(recvAddress, orderRecvAddress);

        orderRecvAddress.setOrderId(orderId);

        if (addressMapper.insert(orderRecvAddress) <= 0) {
            throw new DBOperationException("订单收货地址保存失败！");
        }

        return true;
    }

    /**
     * 计算订单的总运费，这里默认时10元
     * todo 后续应该根据商品的重量和运费模版计算
     *
     * @param reqOrderItems
     * @param orderProductDetailMap
     * @return
     */
    private BigDecimal calcOrderFreightAmount(List<OmsOrderRequestParam.OrderItems> reqOrderItems, Map<Long, PmsProduct> orderProductDetailMap) {
        return BigDecimal.valueOf(10);
    }

    /**
     * 计算订单商品的总金额
     *
     * @param orderItems
     * @param orderProductDetailMap
     * @return
     */
    private BigDecimal calcOrderItemTotalAmount(List<OmsOrderRequestParam.OrderItems> orderItems, Map<Long, PmsProduct> orderProductDetailMap) {
        BigDecimal orderTotalAmount = new BigDecimal(0L);
        for (OmsOrderRequestParam.OrderItems orderItem : orderItems) {
            // 查找商品详情
            PmsProduct productDetail = orderProductDetailMap.get(orderItem.getItemId());
            if (productDetail == null) {
                continue;
            }

            // 总金额加上商品金额 × 商品数量
            orderTotalAmount = orderTotalAmount.add(productDetail.getSalePrice().multiply(BigDecimal.valueOf(orderItem.getItemQuantity())));
        }

        return orderTotalAmount;
    }

    /***
     *  保存订单与商品的关联信息
     *
     * @param orderId
     * @param itemDetailIds
     */
    private boolean saveOrderItem(Long orderId, List<Long> itemDetailIds) {
        for (Long itemDetailId : itemDetailIds) {
            OmsOrderItem orderItem = new OmsOrderItem();
            orderItem.setOrderId(orderId.toString());
            orderItem.setOrderItemDetailId(itemDetailId);

            if (itemMapper.insert(orderItem) <= 0) {
                throw new DBOperationException("订单商品关联失败！");
            }
        }

        return true;
    }

    /**
     * 保存订单商品信息
     *
     * @param orderItems
     * @param orderProductDetailMap
     * @return
     */
    private List<Long> saveOrderItemDetails(List<OmsOrderRequestParam.OrderItems> orderItems, Map<Long, PmsProduct> orderProductDetailMap) {
        ArrayList<Long> itemDetailIds = new ArrayList<>();
        for (OmsOrderRequestParam.OrderItems itemInfo : orderItems) {

            // 从Map中根据商品Id取出查到的商品详情
            PmsProduct product = orderProductDetailMap.get(itemInfo.getItemId());
            if (product == null) {
                continue;
            }

            OmsOrderItemDetail itemDetail = new OmsOrderItemDetail();
            itemDetail.setItemId(Long.valueOf(product.getId()));
            itemDetail.setItemName(product.getTitle());
            itemDetail.setItemPrice(product.getSalePrice());
            itemDetail.setItemQuantity(itemInfo.getItemQuantity());
            if (itemDetailMapper.insert(itemDetail) <= 0) {
                throw new DBOperationException("订单商品详情保存失败！");
            }

            itemDetailIds.add(itemDetail.getId());
        }

        return itemDetailIds;
    }

    /**
     * fixme 订单创建完成后，只有部分字段支持更新
     *
     * @param requestParam
     * @return
     */
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

    @Override
    public List<OmsOrderItemDetail> getOrderItemDetailsByOrderId(Long orderId) {

        List<OmsOrderItem> orderItems = itemMapper.selectList(new QueryWrapper<OmsOrderItem>().eq("order_id", orderId));
        List<Long> itemDetailIds = orderItems.stream().map(OmsOrderItem::getOrderItemDetailId).distinct().collect(Collectors.toList());

        return itemDetailMapper.selectBatchIds(itemDetailIds);
    }

    @Override
    public OmsOrderRecvAddress getOrderRecvAddressByOrderId(Long orderId) {
        return addressMapper.selectOne(new QueryWrapper<OmsOrderRecvAddress>().eq("order_id", orderId));
    }
}
