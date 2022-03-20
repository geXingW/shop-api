package com.gexingw.shop.modules.pms.service;

import com.gexingw.shop.bo.pms.PmsProductSku;
import com.gexingw.shop.modules.pms.dto.product.PmsProductPriceRequestParam;

import java.util.List;

public interface PmsProductSkuService {

    PmsProductSku getById(Long id);

    PmsProductSku getByProductIdAndSkuData(String productId, List<PmsProductPriceRequestParam.Option> spData);

    boolean lockStockBySkuId(Long id, Integer quantity);
}
