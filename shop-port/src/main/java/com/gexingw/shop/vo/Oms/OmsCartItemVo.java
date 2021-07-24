package com.gexingw.shop.vo.Oms;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OmsCartItemVo {
    private Long id;

    private Long memberId;

    private Long itemId;

    private BigDecimal itemPrice;

    private Integer itemQuantity;

    private String itemPic;

    private String itemTitle;

    private Integer checked;

    private Integer limitQuantity;
}
