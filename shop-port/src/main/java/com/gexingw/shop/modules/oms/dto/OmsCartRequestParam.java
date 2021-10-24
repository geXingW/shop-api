package com.gexingw.shop.modules.oms.dto;

import com.gexingw.shop.util.AuthUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OmsCartRequestParam {

    private Long id;

    private Long itemId;

    private BigDecimal itemPrice;

    private Integer itemQuantity;

    private String itemPic;

    private String itemTitle;

    private String itemSubTitle;

    private Date createTime;

    private Long memberId;

    private Integer checked;

    public Long getMemberId() {
        return AuthUtil.getAuthId();
    }
}
