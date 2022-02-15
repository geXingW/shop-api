package com.gexingw.shop.modules.oms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OmsOrderRequestParam {
    private Long id;

    private Long memberId;

    private Integer sourceType;

    private Integer status;

    private String note;

    private List<OrderItems> orderItems;

    private RecvAddress recvAddress;

    /**
     * 订单商品信息
     */
    @Data
    public static class OrderItems {
        private String itemId;

        private Integer itemQuantity;
    }

    /**
     * 订单收货地址
     */
    @Data
    public static class RecvAddress {
        private Long recvAddressId;

        private String recvName;

        private String recvPhone;

        private Integer recvPostCode;

        private String recvProvince;

        private String recvCity;

        private String recvRegion;

        private String recvDetailAddress;
    }
}
