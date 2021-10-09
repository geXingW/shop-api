package com.gexingw.shop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.oms.OmsOrder;
import com.gexingw.shop.dto.order.OmsOrderSearchParam;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.service.OrderService;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import com.gexingw.shop.vo.oms.OrderListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping
    public R index(OmsOrderSearchParam searchParam) {
        Page<OmsOrder> page = new Page<>(searchParam.getPage(), searchParam.getSize());
        QueryWrapper<OmsOrder> queryWrapper = new QueryWrapper<>();

        IPage<OmsOrder> searchResult = orderService.search(queryWrapper, page);
        List<OmsOrder> searchResultRecords = searchResult.getRecords();

        ArrayList<OrderListVO> orderListVOS = new ArrayList<>();
        for (OmsOrder record : searchResultRecords) {
            OrderListVO orderListVO = new OrderListVO(Long.valueOf(record.getId()), record.getMemberId(), record.getTotalAmount(), record.getItemAmount(),
                    record.getFreightAmount(), record.getPayType(), record.getSourceType(), record.getSourceType(), record.getStatus(),
                    record.getNote(), record.getCreateTime(), record.getUpdateTime());

            // 订单商品详情
            orderListVO.setItems(orderService.getOrderItemDetailsByOrderId(Long.valueOf(record.getId())));

            // 订单收货地址
            orderListVO.setRecvAddress(orderService.getOrderRecvAddressByOrderId(Long.valueOf(record.getId())));

            orderListVOS.add(orderListVO);
        }

        // 对返回结果做统一格式化，并替换records信息
        Map<String, Object> formatResult = PageUtil.format(searchResult);
        formatResult.put("records", orderListVOS);

        return R.ok(formatResult);
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
