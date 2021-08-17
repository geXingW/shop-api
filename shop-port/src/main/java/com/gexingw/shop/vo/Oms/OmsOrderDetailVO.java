package com.gexingw.shop.vo.Oms;

import com.gexingw.shop.bo.oms.OmsOrderItemDetail;
import com.gexingw.shop.bo.oms.OmsOrderRecvAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class OmsOrderDetailVO {
    private Long id;

    private Long memberId;

    private Integer sourceType;

    private Integer status = 0;

    private String note;

    private BigDecimal totalAmount;

    private BigDecimal itemAmount;

    private BigDecimal freightAmount;

    private List<OrderItem> orderItems = new ArrayList<>();

    private RecvAddress recvAddress;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class OrderItem {
        private Long itemId;

        private String itemName;

        private Integer itemQuantity;

        private BigDecimal itemPrice;

        private BigDecimal itemAmount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class RecvAddress {
        private String recvName;

        private String recvPhone;

        private String recvDetailAddress;
    }

    public void setOrderItems(List<OmsOrderItemDetail> orderItemDetails) {
        for (OmsOrderItemDetail orderItemDetail : orderItemDetails) {
            OrderItem orderItem = new OrderItem();
            orderItem.setItemId(orderItemDetail.getItemId());
            orderItem.setItemName(orderItemDetail.getItemName());
            orderItem.setItemQuantity(orderItemDetail.getItemQuantity());
            orderItem.setItemPrice(orderItemDetail.getItemPrice());
            orderItem.setItemAmount(orderItemDetail.getItemPrice().multiply(BigDecimal.valueOf(orderItem.getItemQuantity())));

            orderItems.add(orderItem);
        }
    }

    public void setRecvAddress(OmsOrderRecvAddress orderRecvAddress) {
        String fullAddress = orderRecvAddress.getRecvProvince() + orderRecvAddress.getRecvCity() + orderRecvAddress.getRecvRegion() + orderRecvAddress.getRecvDetailAddress();
        recvAddress = new RecvAddress(orderRecvAddress.getRecvName(), orderRecvAddress.getRecvPhone(), fullAddress);
    }
}
