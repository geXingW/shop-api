package com.gexingw.shop.modules.oms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.oms.OmsOrder;
import com.gexingw.shop.bo.oms.OmsOrderItemDetail;
import com.gexingw.shop.bo.oms.OmsOrderRecvAddress;
import com.gexingw.shop.bo.ums.UmsMember;
import com.gexingw.shop.config.FileConfig;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.modules.oms.dto.order.OmsOrderSearchParam;
import com.gexingw.shop.modules.oms.service.OrderService;
import com.gexingw.shop.modules.oms.vo.OrderDetailVO;
import com.gexingw.shop.modules.oms.vo.OrderListVO;
import com.gexingw.shop.modules.ums.service.UmsMemberService;
import com.gexingw.shop.service.OmsOrderRecvAddressService;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    FileConfig fileConfig;

    @Autowired
    OrderService orderService;

    @Autowired
    UmsMemberService memberService;

    @Autowired
    OmsOrderRecvAddressService recvAddressService;

    @GetMapping
    public R index(OmsOrderSearchParam searchParam) {
        Page<OmsOrder> page = new Page<>(searchParam.getPage(), searchParam.getSize());
        QueryWrapper<OmsOrder> queryWrapper = new QueryWrapper<>();

        IPage<OmsOrder> searchResult = orderService.search(queryWrapper, page);
        List<OmsOrder> searchResultRecords = searchResult.getRecords();

        ArrayList<OrderListVO> orderListVOS = new ArrayList<>();
        for (OmsOrder record : searchResultRecords) {
            OrderListVO orderListVO = new OrderListVO(record.getId(), record.getMemberId(), record.getTotalAmount(), record.getItemAmount(),
                    record.getFreightAmount(), record.getPayType(), record.getSourceType(), record.getSourceType(), record.getStatus(),
                    record.getNote(), record.getCreateTime(), record.getUpdateTime());

            // 订单商品详情
            orderListVO.setItems(orderService.getOrderItemDetailsByOrderId(record.getId()));

            // 订单收货地址
            orderListVO.setRecvAddress(orderService.getOrderRecvAddressByOrderId(record.getId()));

            orderListVOS.add(orderListVO);
        }

        // 对返回结果做统一格式化，并替换records信息
        Map<String, Object> formatResult = PageUtil.format(searchResult);
        formatResult.put("records", orderListVOS);

        return R.ok(formatResult);
    }

    @GetMapping("/{id}")
    public R show(@PathVariable String id) {
        OmsOrder order = orderService.findById(id);
        if (order == null) {
            return R.ok(RespCode.RESOURCE_NOT_EXIST.getCode(), "订单未找到！");
        }

        OrderDetailVO orderDetailVO = new OrderDetailVO();

        // 订单信息
        BeanUtils.copyProperties(order, orderDetailVO);

        // 订单用户信息
        UmsMember umsMember = memberService.getById(order.getMemberId());
        orderDetailVO.setMember(umsMember);

        // 订单收货人信息
        OmsOrderRecvAddress recvAddress = recvAddressService.getOne(new QueryWrapper<OmsOrderRecvAddress>().eq("order_id", order.getId()));
        orderDetailVO.setRecvAddress(recvAddress);

        // 订单商品信息
        List<OmsOrderItemDetail> orderItems = orderService.getOrderItemDetailsByOrderId(id);
        orderDetailVO.setItems(orderItems, fileConfig);

        return R.ok(orderDetailVO);
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
