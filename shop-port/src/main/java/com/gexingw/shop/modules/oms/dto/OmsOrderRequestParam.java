package com.gexingw.shop.modules.oms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OmsOrderRequestParam {
    private String id;

    private Long memberId;

    private Integer sourceType;

    private Integer payType;

    private Integer status;

    private String note;

    private List<OrderItem> orderItems;

    private Long addressId;

    /**
     * 订单商品信息
     */
    @Data
    public static class OrderItem {
        private Long cartId;

        private String itemId;

        private Long skuId;

        private Integer itemQuantity;

    }

}
