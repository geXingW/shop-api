package com.gexingw.shop.modules.oms.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gexingw.shop.bo.oms.OmsOrderItemDetail;
import com.gexingw.shop.bo.oms.OmsOrderRecvAddress;
import com.gexingw.shop.bo.ums.UmsMember;
import com.gexingw.shop.constant.SystemConstant;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class OrderListVO {
    private String id;

    private Long memberId;

    private BigDecimal totalAmount;

    private BigDecimal itemAmount;

    private BigDecimal freightAmount;

    private BigDecimal payAmount;

    private Integer payType;

    private Integer sourceType;

    private Integer status;

    private String note;

    @JsonFormat(pattern = SystemConstant.DATETIME_STRING_FORMAT, timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = SystemConstant.DATETIME_STRING_FORMAT, timezone = "GMT+8")
    private Date updateTime;

    private List<ItemDetailVo> items = new ArrayList<>();

    private RecvAddressVo recvAddress;

    private UmsMember member;

    public OrderListVO(String id, Long memberId, BigDecimal totalAmount, BigDecimal itemAmount, BigDecimal freightAmount, Integer payType, Integer sourceType, Integer sourceType1, Integer status, String note, Date createTime, Date updateTime) {
        this.setId(id);
        this.setMemberId(memberId);
        this.setTotalAmount(totalAmount);
        this.setItemAmount(itemAmount);
        this.setFreightAmount(freightAmount);
        this.setPayAmount(payAmount);
        this.setPayType(payType);
        this.setSourceType(sourceType);
        this.setStatus(status);
        this.setNote(note);
        this.setCreateTime(createTime);
        this.setUpdateTime(updateTime);
    }

    @Data
    @AllArgsConstructor
    private static class ItemDetailVo {
        private String id;

        private String name;

        private Integer quantity;

        private BigDecimal price;
    }

    @Data
    private static class RecvAddressVo {
        private String name;

        private String phone;

        private Integer postCode;

        private String province;

        private String city;

        private String region;

        private String detailAddress;
    }

    @Data
    public static class MemberVO {
        private String name;
    }

    public void setItems(List<OmsOrderItemDetail> itemDetails) {
        for (OmsOrderItemDetail itemDetail : itemDetails) {
            items.add(new ItemDetailVo(itemDetail.getId(), itemDetail.getItemName(), itemDetail.getItemQuantity(), itemDetail.getItemPrice()));
        }
    }

    public void setRecvAddress(OmsOrderRecvAddress orderRecvAddress) {
        this.recvAddress = new RecvAddressVo();
        recvAddress.setName(orderRecvAddress.getName());
        recvAddress.setPhone(orderRecvAddress.getPhoneNumber());
        recvAddress.setPostCode(orderRecvAddress.getPostCode());
        recvAddress.setProvince(orderRecvAddress.getProvinceName());
        recvAddress.setCity(orderRecvAddress.getCityName());
        recvAddress.setRegion(orderRecvAddress.getRegionName());
        recvAddress.setDetailAddress(orderRecvAddress.getDetailAddress());
    }

}
