package com.gexingw.shop.modules.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gexingw.shop.bo.oms.OmsOrderItemDetail;

import java.util.List;

public interface OmsOrderItemDetailService extends IService<OmsOrderItemDetail> {

    List<OmsOrderItemDetail> getListByIds(List<String> orderItemIds);
}
