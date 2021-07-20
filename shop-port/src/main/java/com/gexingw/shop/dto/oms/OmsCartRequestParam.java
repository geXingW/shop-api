package com.gexingw.shop.dto.oms;

import com.gexingw.shop.util.AuthUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OmsCartRequestParam {
    private Long itemId;

    private BigDecimal itemPrice;

    private Integer itemQuantity;

    private String itemPic;

    private String itemTitle;

    private String itemSubTitle;

    private Date createTime;

    private Long memberId;

    public Long getMemberId() {
        return AuthUtil.getAuthId();
    }
}
