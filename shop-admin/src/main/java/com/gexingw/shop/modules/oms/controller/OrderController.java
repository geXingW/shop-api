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

            // ??????????????????
            orderListVO.setItems(orderService.getOrderItemDetailsByOrderId(record.getId()));

            // ??????????????????
            orderListVO.setRecvAddress(orderService.getOrderRecvAddressByOrderId(record.getId()));

            orderListVOS.add(orderListVO);
        }

        // ?????????????????????????????????????????????records??????
        Map<String, Object> formatResult = PageUtil.format(searchResult);
        formatResult.put("records", orderListVOS);

        return R.ok(formatResult);
    }

    @GetMapping("/{id}")
    public R show(@PathVariable String id) {
        OmsOrder order = orderService.findById(id);
        if (order == null) {
            return R.failure(RespCode.ORDER_NOT_EXIST);
        }

        OrderDetailVO orderDetailVO = new OrderDetailVO();

        // ????????????
        BeanUtils.copyProperties(order, orderDetailVO);

        // ??????????????????
        UmsMember umsMember = memberService.getById(order.getMemberId());
        orderDetailVO.setMember(umsMember);

        // ?????????????????????
        OmsOrderRecvAddress recvAddress = recvAddressService.getOne(new QueryWrapper<OmsOrderRecvAddress>().eq("order_id", order.getId()));
        orderDetailVO.setRecvAddress(recvAddress);

        // ??????????????????
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
            return R.failure(RespCode.DELETE_FAILURE);
        }

        return R.ok();
    }
}
