package com.gexingw.shop.dto.oms;

import com.baomidou.mybatisplus.annotation.TableId;
import com.gexingw.shop.bo.oms.OmsOrderItemDetail;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class OmsOrderRequestParam {
    private Long id;

    private Long memberId;

//    private BigDecimal totalAmount;
//
//    private BigDecimal freightAmount;
//
//    private BigDecimal payAmount;

//    private Integer payType;

    private Integer sourceType;

//    private Integer status;

    private String note;

    private Integer recvPostCode;

    private String recvProvince;

    private String recvCity;

    private String recvRegion;

    private List<OmsOrderItemRequestParam> orderItems;

    @Data
    public class OmsOrderItemRequestParam {
        private Long itemId;

        private Integer itemQuantity;
    }
}
