package com.gexingw.shop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.oms.OmsOrder;
import com.gexingw.shop.dto.order.OmsOrderSearchParam;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.service.OrderService;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
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
        Page<OmsOrder> page = new Page<>(searchParam.getPage(), searchParam.getSize());
        QueryWrapper<OmsOrder> queryWrapper = new QueryWrapper<>();

        return R.ok(PageUtil.format(orderService.search(queryWrapper, page)));
    }

    @GetMapping("/{id}")
    public R show(@PathVariable Long id) {
        OmsOrder order = orderService.findById(id);
        if (order == null) {
            return R.ok(RespCode.RESOURCE_NOT_EXIST.getCode(), "订单未找到！");
        }

        return R.ok(order);
    }

    @PutMapping
    public R update() {
        return R.ok();
    }

    @DeleteMapping
    public R delete(@RequestBody Set<Long> ids) {
        if (!orderService.deleteByOrderIds(ids)) {
            return R.ok(RespCode.DELETE_FAILURE.getCode(), "删除失败！");
        }

        return R.ok();
    }
}
