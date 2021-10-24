package com.gexingw.shop.modules.oms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.oms.OmsOrder;
import com.gexingw.shop.modules.oms.dto.OmsOrderRequestParam;
import com.gexingw.shop.modules.oms.dto.OmsOrderSearchParam;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.modules.oms.service.OrderService;
import com.gexingw.shop.util.AuthUtil;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import com.gexingw.shop.modules.oms.vo.OmsOrderDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping
    public R index(OmsOrderSearchParam searchParam) {
        IPage<OmsOrder> page = new Page<>(searchParam.getPage(), searchParam.getSize());

        QueryWrapper<OmsOrder> queryWrapper = new QueryWrapper<>();
        if (searchParam.getOrderId() != null) {
            queryWrapper.eq("id", searchParam.getOrderId());
        }

        return R.ok(PageUtil.format(orderService.search(queryWrapper, page)));
    }

    @GetMapping("/{id}")
    public R show(@PathVariable Long id) {
        OmsOrder order = orderService.findById(id);
        if (order == null) {
            return R.ok(RespCode.RESOURCE_NOT_EXIST.getCode(), "未找到该订单！");
        }

        OmsOrderDetailVO orderDetailVO = new OmsOrderDetailVO();
        orderDetailVO.setId(order.getId());
        orderDetailVO.setMemberId(order.getMemberId());
        orderDetailVO.setStatus(order.getStatus());
        orderDetailVO.setTotalAmount(order.getTotalAmount());
        orderDetailVO.setItemAmount(order.getItemAmount());
        orderDetailVO.setFreightAmount(order.getFreightAmount());

        // 查询订单商品信息
        orderDetailVO.setOrderItems(orderService.getOrderItemDetailsByOrderId(id));

        orderDetailVO.setRecvAddress(orderService.getOrderRecvAddressByOrderId(id));

        return R.ok(orderDetailVO);
    }

    @PostMapping
    public R save(@RequestBody OmsOrderRequestParam requestParam) {
        // 设置订单用户
        requestParam.setMemberId(AuthUtil.getAuthId());

        Long orderId;   // 订单Id

        // 保存订单
        try {
            orderId = orderService.save(requestParam);
        } catch (Exception e) {
            return R.ok(RespCode.SAVE_FAILURE.getCode(), e.getMessage());
        }

        if (orderId == null) {
            return R.ok(RespCode.SAVE_FAILURE.getCode(), "订单创建失败！");
        }

        return R.ok(orderId, "已创建！");
    }

    @PutMapping
    public R update(@RequestBody OmsOrderRequestParam requestParam) {
        if (!orderService.update(requestParam)) {
            return R.ok(RespCode.UPDATE_FAILURE.getCode(), "更新失败！");
        }

        // todo 清除redis缓存

        return R.ok("已更新！");
    }

    @DeleteMapping
    public R delete(@RequestBody Set<Long> ids) {
        return orderService.deleteByIds(ids) ? R.ok("已删除！") : R.ok(RespCode.DELETE_FAILURE.getCode(), "删除失败！");
    }
}
