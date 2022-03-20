package com.gexingw.shop.modules.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.oms.*;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.bo.pms.PmsProductSku;
import com.gexingw.shop.bo.ums.UmsMemberRecvAddress;
import com.gexingw.shop.exception.DBOperationException;
import com.gexingw.shop.exception.ResourceNotExistException;
import com.gexingw.shop.mapper.oms.OmsOrderItemDetailMapper;
import com.gexingw.shop.mapper.oms.OmsOrderItemMapper;
import com.gexingw.shop.mapper.oms.OmsOrderMapper;
import com.gexingw.shop.mapper.oms.OmsOrderRecvAddressMapper;
import com.gexingw.shop.mapper.ums.UmsMemberRecvAddressMapper;
import com.gexingw.shop.modules.oms.dto.OmsOrderRequestParam;
import com.gexingw.shop.modules.oms.service.CartService;
import com.gexingw.shop.modules.oms.service.OmsOrderService;
import com.gexingw.shop.modules.pms.service.PmsProductService;
import com.gexingw.shop.modules.pms.service.PmsProductSkuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OmsOrderServiceImpl implements OmsOrderService {

    @Autowired
    OmsOrderMapper orderMapper;

    @Autowired
    OmsOrderItemDetailMapper itemDetailMapper;

    @Autowired
    OmsOrderItemMapper itemMapper;

    @Autowired
    PmsProductService productService;

    @Autowired
    PmsProductSkuService productSkuService;

    @Autowired
    UmsMemberRecvAddressMapper recvAddressMapper;

    @Autowired
    OmsOrderRecvAddressMapper orderAddressMapper;

    @Autowired
    CartService cartService;

    @Override
    public IPage<OmsOrder> search(QueryWrapper<OmsOrder> queryWrapper, IPage<OmsOrder> page) {
        return orderMapper.selectPage(page, queryWrapper);
    }

    @Override
    public OmsOrder getById(String id) {
        return orderMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(OmsOrderRequestParam requestParam) {
        // 订单
        OmsOrder order = new OmsOrder();

        BeanUtils.copyProperties(requestParam, order);

        // 用户端传来的商品信息
        List<OmsOrderRequestParam.OrderItem> reqOrderItems = requestParam.getOrderItems();

        // 订单商品总价
        BigDecimal itemsTotalAmount = new BigDecimal(0);

        ArrayList<OmsCartItem> cartItems = new ArrayList<>();

        for (OmsOrderRequestParam.OrderItem reqOrderItem : reqOrderItems) {
            // 检查商品信息
            OmsCartItem cartItem = cartService.getById(reqOrderItem.getCartId());
            if (cartItem == null) {
                throw new ResourceNotExistException("购物车商品已下架！");
            }

            cartItems.add(cartItem);

            // 检查商品信息
            PmsProduct product = productService.getById(cartItem.getItemId());
            if (product == null) {
                throw new ResourceNotExistException("购物车商品已下架！");
            }

            boolean stockLocked = false;

            // 如果存在商品Sku，检查商品Sku信息
            if (reqOrderItem.getSkuId() != null) {
                PmsProductSku productSku = productSkuService.getById(cartItem.getSkuId());
                if (productSku == null) {
                    throw new ResourceNotExistException("购物车商品规格错误！");
                }
                // 扣除sku库存
                stockLocked = productSkuService.lockStockBySkuId(productSku.getId(), cartItem.getItemQuantity());

                // 购物车商品总价加上 商品单价 × 数量
                itemsTotalAmount = itemsTotalAmount.add(productSku.getPrice().multiply(new BigDecimal(cartItem.getItemQuantity())));
            } else { // 如果没有Sku，扣除商品库存
                stockLocked = productService.lockStockByProductId(product.getId(), cartItem.getItemQuantity());

                // 购物车商品总价加上 商品单价 × 数量
                itemsTotalAmount = itemsTotalAmount.add(product.getSalePrice().multiply(new BigDecimal(cartItem.getItemQuantity())));
            }

            // 库存锁定失败，抛出异常
            if (!stockLocked) {
                throw new DBOperationException("库存不足！");
            }

        }

        // 订单商品总额
        order.setItemAmount(itemsTotalAmount);

        // 订单总运费
        order.setFreightAmount(calcOrderFreightAmount(cartItems));

        // 计算订单金额 = 订单商品总金额 + 订单运费总金额
        order.setTotalAmount(order.getItemAmount().add(order.getFreightAmount()));

        // 订单应付总金额
        order.setPayAmount(order.getTotalAmount());

        // 保存订单信息
        if (orderMapper.insert(order) <= 0) {
            throw new DBOperationException("订单信息保存失败！");
        }

        // 订单Item信息
        List<String> itemDetailIds = saveOrderItemDetails(cartItems);

        // 进行订单id和订单商品id进行绑定
        saveOrderItem(Long.valueOf(order.getId()), itemDetailIds);

        // 保存订单收货地址信息
        if (!saveOrderRecvAddress(order.getId(), requestParam.getAddressId())) {
            throw new DBOperationException("订单收获地址保存失败！");
        }

        // 移除购物车中的商品
        if (!cartService.delCartItemsByIds(cartItems.stream().map(OmsCartItem::getId).collect(Collectors.toList()))) {
            throw new DBOperationException("删除购物车商品失败！");
        }

        // 增加商品的销量
        if (!productService.addProductSaleCount(cartItems)) {
            throw new DBOperationException("商品销量更新失败！");
        }

        return Long.valueOf(order.getId());
    }

    /**
     * 保存订单收货地址信息
     *
     * @param orderId
     * @param addressId
     * @return
     */
    private boolean saveOrderRecvAddress(String orderId, Long addressId) {
        UmsMemberRecvAddress memberRecvAddress = recvAddressMapper.selectById(addressId);
        if (memberRecvAddress == null) {
            return false;
        }

        OmsOrderRecvAddress orderRecvAddress = new OmsOrderRecvAddress();
        BeanUtils.copyProperties(memberRecvAddress, orderRecvAddress);

        orderRecvAddress.setOrderId(orderId);
        orderRecvAddress.setRecvAddressId(addressId);

        return orderAddressMapper.insert(orderRecvAddress) > 0;
    }

    /**
     * 计算订单的总运费，这里默认时10元
     * todo 后续应该根据商品的重量和运费模版计算
     *
     * @param cartItems 订单购物车信息
     * @return 订单总运费
     */
    private BigDecimal calcOrderFreightAmount(List<OmsCartItem> cartItems) {
        return new BigDecimal(10);
    }

    /***
     *  保存订单与商品的关联信息
     *
     * @param orderId 订单ID
     * @param itemDetailIds 订单详情ID数组
     */
    private boolean saveOrderItem(Long orderId, List<String> itemDetailIds) {
        for (String itemDetailId : itemDetailIds) {
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
     * @param cartItems 购物车商品详情
     * @return 保存的订单详情ID
     */
    private List<String> saveOrderItemDetails(List<OmsCartItem> cartItems) {
        ArrayList<String> itemDetailIds = new ArrayList<>();
        for (OmsCartItem cartItem : cartItems) {

            OmsOrderItemDetail itemDetail = new OmsOrderItemDetail();
            itemDetail.setItemId(cartItem.getItemId());
            itemDetail.setItemName(cartItem.getItemTitle());
            itemDetail.setItemPrice(cartItem.getItemPrice());
            itemDetail.setItemQuantity(cartItem.getItemQuantity());
            itemDetail.setItemPic(cartItem.getItemPic());

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
        OmsOrder order = getById(requestParam.getId());
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
        List<String> itemDetailIds = itemMapper.selectList(new QueryWrapper<OmsOrderItem>().in("order_id", ids))
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
    public List<OmsOrderItemDetail> getOrderItemDetailsByOrderId(String orderId) {

        List<OmsOrderItem> orderItems = itemMapper.selectList(new QueryWrapper<OmsOrderItem>().eq("order_id", orderId));
        List<String> itemDetailIds = orderItems.stream().map(OmsOrderItem::getOrderItemDetailId).distinct().collect(Collectors.toList());

        return itemDetailMapper.selectBatchIds(itemDetailIds);
    }

    @Override
    public OmsOrderRecvAddress getOrderRecvAddressByOrderId(String orderId) {
        return orderAddressMapper.selectOne(new QueryWrapper<OmsOrderRecvAddress>().eq("order_id", orderId));
    }
}
