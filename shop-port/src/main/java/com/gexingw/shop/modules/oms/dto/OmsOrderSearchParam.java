package com.gexingw.shop.modules.oms.dto;

import com.gexingw.shop.dto.BaseSearchParam;
import lombok.Data;

@Data
public class OmsOrderSearchParam extends BaseSearchParam {
    private Long orderId;
}
