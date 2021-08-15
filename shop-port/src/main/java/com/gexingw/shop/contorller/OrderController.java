package com.gexingw.shop.contorller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.oms.OmsOrder;
import com.gexingw.shop.dto.oms.OmsOrderRequestParam;
import com.gexingw.shop.dto.oms.OmsOrderSearchParam;
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

        return R.ok(order);
    }

    @PostMapping
    public R save(@RequestBody OmsOrderRequestParam requestParam) {
        Long orderId = orderService.save(requestParam);
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
