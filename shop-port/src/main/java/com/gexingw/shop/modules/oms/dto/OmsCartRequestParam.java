package com.gexingw.shop.modules.oms.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gexingw.shop.constant.SystemConstant;
import com.gexingw.shop.util.AuthUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class OmsCartRequestParam {

    private Long id;

    private Long memberId;

    private String productId;

    private Integer productCnt;

    private Long skuId;

    private List<SkuItem> skuData = new ArrayList<>();

    private Integer checked;

    public Long getMemberId() {
        return AuthUtil.getAuthId();
    }

    @Data
    public static class SkuItem {
        private int attributeId;

        private String attributeName;

        private String attributeValue;
    }
}
