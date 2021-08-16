package com.gexingw.shop.dto.oms;

import com.baomidou.mybatisplus.annotation.TableId;
import com.gexingw.shop.bo.oms.OmsOrderItemDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class OmsOrderRequestParam {
    private Long id;

    private Long memberId;

    private Integer sourceType;

    private Integer status = 0;

    private String note;

    private List<OrderItems> orderItems;

    private RecvAddress recvAddress;

    /**
     * 订单商品信息
     */
    @Data
    public static class OrderItems {
        private Long itemId;

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
