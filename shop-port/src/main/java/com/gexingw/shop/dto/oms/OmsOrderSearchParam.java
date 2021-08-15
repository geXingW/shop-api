package com.gexingw.shop.dto.oms;

import com.gexingw.shop.dto.BaseSearchParam;
import lombok.Data;

@Data
public class OmsOrderSearchParam extends BaseSearchParam {
    private Long orderId;
}
