package com.gexingw.shop.modules.oms.service;

import java.util.List;

public interface OmsOrderItemService {

    List<String> getItemDetailIdsByOrderId(String orderId);
}
