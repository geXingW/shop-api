package com.gexingw.shop.modules.pms.service;

import com.gexingw.shop.bo.pms.PmsProductSku;
import com.gexingw.shop.modules.pms.dto.product.PmsProductPriceRequestParam;

import java.util.List;

public interface PmsProductSkuService {

    PmsProductSku getByIdAndSkuData(String id, List<PmsProductPriceRequestParam.Option> spData);
}
