package com.gexingw.shop.modules.oms.vo;

import cn.hutool.core.bean.BeanUtil;
import com.gexingw.shop.bo.oms.OmsOrder;
import com.gexingw.shop.bo.oms.OmsOrderItemDetail;
import com.gexingw.shop.bo.oms.OmsOrderRecvAddress;
import com.gexingw.shop.bo.ums.UmsMember;
import com.gexingw.shop.config.FileConfig;
import com.gexingw.shop.utils.FileUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDetailVO {
    private String id;

    private BigDecimal totalAmount;

    private BigDecimal itemAmount;

    private BigDecimal freightAmount;

    private BigDecimal payAmount;

    private Integer payType;

    private Integer sourceType;

    private Integer status;

    private String note;

    private Member member;

    private RecvAddress recvAddress;

    private List<Item> items = new ArrayList<>();

    @Data
    private static class Member {
        private String name;
    }

    @Data
    private static class RecvAddress {

        private String name;

        private String phone;

        private String postCode;

        private String detailAddress;
    }

    @Data
    private static class Item {

        private String id;

        private String name;

        private String pic;

        private String skuInfo;

        private Integer quantity;

        private BigDecimal price;

        private BigDecimal totalAmount;
    }

    public OrderDetailVO setInfo(OmsOrder order) {
        BeanUtil.copyProperties(order, this);
        return this;
    }

    public OrderDetailVO setItems(List<OmsOrderItemDetail> orderItems, FileConfig fileConfig) {
        for (OmsOrderItemDetail orderItem : orderItems) {
            Item item = new Item();
            item.setId(orderItem.getItemId());
            item.setName(orderItem.getItemName());
            item.setPic(FileUtil.buildFileFullUrl(fileConfig, orderItem.getItemPic()));
            item.setQuantity(orderItem.getItemQuantity());
            item.setPrice(orderItem.getItemPrice());
            item.setTotalAmount(new BigDecimal(0));
            this.items.add(item);
        }

        return this;
    }

    public OrderDetailVO setMember(UmsMember umsMember) {
        Member member = new Member();
        member.setName(umsMember.getUsername());
        this.member = member;

        return this;

    }

    public OrderDetailVO setRecvAddress(OmsOrderRecvAddress orderRecvAddress) {
        RecvAddress recvAddress = new RecvAddress();
        recvAddress.setName(orderRecvAddress.getName());
        recvAddress.setPhone(orderRecvAddress.getPhoneNumber());
        recvAddress.setPostCode(orderRecvAddress.getPostCode().toString());
        recvAddress.setDetailAddress(orderRecvAddress.getFullDetailAddress());

        this.recvAddress = recvAddress;

        return this;
    }
}
