package com.gexingw.shop.modules.oms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.oms.OmsOrder;
import com.gexingw.shop.bo.oms.OmsOrderItemDetail;
import com.gexingw.shop.bo.oms.OmsOrderRecvAddress;
import com.gexingw.shop.config.FileConfig;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.modules.oms.dto.OmsOrderRequestParam;
import com.gexingw.shop.modules.oms.dto.OmsOrderSearchParam;
import com.gexingw.shop.modules.oms.service.OmsOrderItemDetailService;
import com.gexingw.shop.modules.oms.service.OmsOrderItemService;
import com.gexingw.shop.modules.oms.service.OmsOrderRecvAddressService;
import com.gexingw.shop.modules.oms.service.OmsOrderService;
import com.gexingw.shop.modules.oms.vo.OmsOrderDetailVO;
import com.gexingw.shop.modules.oms.vo.OmsOrderListVO;
import com.gexingw.shop.util.AuthUtil;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("order")
public class OmsOrderController {

    @Autowired
    OmsOrderService orderService;

    @Autowired
    OmsOrderItemService orderItemService;

    @Autowired
    OmsOrderItemDetailService orderItemDetailService;

    @Autowired
    OmsOrderRecvAddressService orderRecvAddressService;

    @Autowired
    FileConfig fileConfig;

    @GetMapping
    public R index(OmsOrderSearchParam searchParam) {
        IPage<OmsOrder> page = new Page<>(searchParam.getPage(), searchParam.getSize());

        QueryWrapper<OmsOrder> queryWrapper = new QueryWrapper<>();

        // 按照下单时间降序
        queryWrapper.orderByDesc("create_time");
        // 根据订单号查询
        if (searchParam.getOrderId() != null) {
            queryWrapper.eq("id", searchParam.getOrderId());
        }

        // 根据订单状态查询
        if (searchParam.getStatus() != -1) {
            queryWrapper.eq("status", searchParam.getStatus());
        }

        // 根据下单日期查询
        if (searchParam.getDateStart() != null) {
            queryWrapper.ge("create_time", searchParam.getDateStart());
        }
        if (searchParam.getDateEnd() != null) {
            queryWrapper.le("create_time", searchParam.getDateEnd());
        }

        ArrayList<OmsOrderListVO> orderListVOs = new ArrayList<>();

        IPage<OmsOrder> orderPage = orderService.search(queryWrapper, page);
        List<OmsOrder> records = orderPage.getRecords();

        for (OmsOrder order : records) {
            OmsOrderListVO orderListVO = new OmsOrderListVO();

            // 订单基本信息
            orderListVO.setId(order.getId());
            orderListVO.setPayType(order.getPayType());
            orderListVO.setStatus(order.getStatus());
            orderListVO.setPayAmount(order.getPayAmount());

            // 订单商品信息
            List<String> orderItemIds = orderItemService.getItemDetailIdsByOrderId(order.getId());
            List<OmsOrderItemDetail> orderItemDetails = orderItemDetailService.getListByIds(orderItemIds);
            orderListVO.setOrderItems(orderItemDetails, fileConfig);

            // 订单收获地址信息
            OmsOrderRecvAddress orderRecvAddress = orderRecvAddressService.findByOrderId(order.getId());
            orderListVO.setOrderRecvAddress(orderRecvAddress);

            orderListVOs.add(orderListVO);
        }

        Map<String, Object> searchResult = PageUtil.format(orderPage);
        searchResult.put("records", orderListVOs);

        return R.ok(searchResult);
    }

    @GetMapping("/{id}")
    public R show(@PathVariable String id) {
        OmsOrder order = orderService.getById(id);
        if (order == null) {
            return R.ok(RespCode.RESOURCE_NOT_EXIST);
        }

        OmsOrderDetailVO orderDetailVO = new OmsOrderDetailVO();
        orderDetailVO.setId(order.getId());
        orderDetailVO.setMemberId(order.getMemberId());
        orderDetailVO.setStatus(order.getStatus());
        orderDetailVO.setTotalAmount(order.getTotalAmount());
        orderDetailVO.setItemAmount(order.getItemAmount());
        orderDetailVO.setFreightAmount(order.getFreightAmount());
        orderDetailVO.setPayAmount(order.getPayAmount());

        // 查询订单商品信息
        orderDetailVO.setOrderItems(orderService.getOrderItemDetailsByOrderId(id), fileConfig);

        orderDetailVO.setRecvAddress(orderService.getOrderRecvAddressByOrderId(id));

        return R.ok(orderDetailVO);
    }

    @PostMapping
    public R save(@RequestBody OmsOrderRequestParam requestParam) {
        // 设置订单用户
        requestParam.setMemberId(AuthUtil.getAuthId());

        String orderId;   // 订单Id

        // 保存订单
        try {
            orderId = orderService.save(requestParam);
        } catch (Exception e) {
            return R.ok(RespCode.SAVE_FAILURE.getCode(), e.getMessage());
        }

        if (orderId == null) {
            return R.ok(RespCode.SAVE_FAILURE);
        }

        return R.ok(RespCode.ORDER_CREATED, RespCode.ORDER_CREATED.getMessage(), orderId);
    }

    @PutMapping("/{id}")
    public R update(@PathVariable String id, @RequestBody OmsOrderRequestParam requestParam) {
        // 检查订单是否存在
        OmsOrder order = orderService.getById(id);
        if (order == null) {
            return R.ok(RespCode.ORDER_NOT_EXIST);
        }

        return R.ok(RespCode.ORDER_UPDATED);
//
//        if (!orderService.update(requestParam)) {
//            return R.ok(RespCode.UPDATE_FAILURE.getCode(), "更新失败！");
//        }
//
//        return R.ok("已更新！");
    }

    @DeleteMapping
    public R delete(@RequestBody Set<Long> ids) {
        return orderService.deleteByIds(ids) ? R.ok(RespCode.ORDER_DELETED) : R.ok(RespCode.DELETE_FAILURE);
    }
}
